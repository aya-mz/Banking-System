package Facad;
import Transaction.*;
import Transaction.Adapter.*;
import Transaction.Facad.Facad;
import Transaction.TransactionCommand.*;
import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.Account;
import account.AccountType;
import approval.*;
import core.user.AuthInfo;
import core.user.Role;
import core.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FacadTest {

    Account sender;
    Account receiver;
    User adminUser;
    User normalUser;
    ApprovalHandler chain;
    Facad facade;
    TransactionRepository txRepo;

    @Mock
    Account mockAccount;

    @Mock
    Transaction mockTransaction;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // إنشاء repository خارجي ومشارك للاختبارات
        txRepo = new TransactionRepository();

        // Facad يستخدم repository الخارجي
        facade = new Facad(txRepo);

        // Mock الحساب والمعاملة
        sender = new Account(1, "Sender", AccountType.CHECKING, 15000, 1234);
        receiver = new Account(2, "Receiver", AccountType.SAVING, 1000, 0);

        adminUser = new User(1, "admin@test.com", Role.ADMIN, new AuthInfo("admin123"), null);
        normalUser = new User(2, "user@test.com", Role.CUSTOMER, new AuthInfo("user123"), null);

        // إنشاء Approval Chain
        ApprovalHandler teller = new TellerHandler();
        ApprovalHandler manager = new ManagerHandler();
        ApprovalHandler admin = new AdminHandler();
        teller.setNext(manager);
        manager.setNext(admin);
        chain = teller;

        // Mock transaction
        when(mockTransaction.getReciveaccount()).thenReturn(mockAccount);
        when(mockTransaction.getAmount()).thenReturn(100.0);
    }

    @Test
    void testProcessTransactionSuccess_Paypal() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();
        AuditLogObserver audit = new AuditLogObserver();
        NotificationObserver notification = new NotificationObserver();
        publisher.subscribe(audit);
        publisher.subscribe(notification);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        when(mockTransaction.getType()).thenReturn(TransactionType.PAPAL);

        dispatcher.dispatch(
                () -> facade.processTransaction(mockTransaction),
                "PROCESS_TRANSACTION",
                "Paypal transaction processed"
        );

        verify(mockAccount, times(1)).getBalance();
        verify(mockAccount, times(1)).setBalance(anyDouble());
        assertEquals(1, audit.getLogs().size());
        assertEquals(1, txRepo.findAll().size()); // تم تعديل getAll() إلى findAll()
    }

    @Test
    void testUndoLastTransaction() {
        Transaction tx = new Transaction(200, 1, receiver, sender, TransactionType.PAPAL);
        facade.processTransaction(tx);
        assertEquals(1, txRepo.findAll().size());

        facade.undoLast();
        assertEquals(0, txRepo.findAll().size());
    }

    @Test
    void testProcessTransactionWithApproval_Approved() {
        Transaction tx = new Transaction(500, 1, receiver, sender, TransactionType.PAPAL);
        ApprovalRequest request = new ApprovalRequest(tx, adminUser, sender.getPin_code());

        facade.processTransactionWithApproval(tx, request, chain);

        assertFalse(facade.commands.isEmpty());
        assertEquals(1, txRepo.findAll().size());
    }

    @Test
    void testProcessTransactionWithApproval_Rejected() {
        Transaction tx = new Transaction(500, 1, receiver, sender, TransactionType.PAPAL);
        ApprovalRequest request = new ApprovalRequest(tx, normalUser, 9999); // رفض

        facade.processTransactionWithApproval(tx, request, chain);

        assertTrue(facade.commands.isEmpty());
        assertEquals(0, txRepo.findAll().size());
    }

    @Test
    void testRegisterNewTransactionType() {
        TransactionType NEW_TYPE = TransactionType.valueOf("VISA");
        Transaction newTx = new Transaction(50, 1, receiver, sender, NEW_TYPE);

        facade.registerTransactionType(
                NEW_TYPE,
                t -> new payment(t, new paypalAdapter(new paypal()))
        );

        facade.processTransaction(newTx);

        assertEquals(1, txRepo.findAll().size());
        assertEquals(NEW_TYPE, txRepo.findAll().get(0).getType());
    }
}
