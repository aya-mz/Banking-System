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

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        txRepo = new TransactionRepository();
        facade = new Facad(txRepo);

        sender = new Account(1, "Sender", AccountType.CHECKING, 15000, 1234);
        receiver = new Account(2, "Receiver", AccountType.SAVING, 1000, 0);

        adminUser = new User(1, "admin@test.com", Role.ADMIN, new AuthInfo("admin123"), null);
        normalUser = new User(2, "user@test.com", Role.CUSTOMER, new AuthInfo("user123"), null);

        ApprovalHandler teller = new TellerHandler();
        ApprovalHandler manager = new ManagerHandler();
        ApprovalHandler admin = new AdminHandler();
        teller.setNext(manager);
        manager.setNext(admin);
        chain = teller;

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
        assertEquals(1, txRepo.findAll().size());
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
    void testRegisterTransactionType_existingType() {
        Transaction mockTx = mock(Transaction.class);
        when(mockTx.getType()).thenReturn(TransactionType.PAPAL);
        when(mockTx.getReciveaccount()).thenReturn(receiver);
        when(mockTx.getAmount()).thenReturn(50.0);

        facade.registerTransactionType(TransactionType.PAPAL,
                t -> new payment(t, new paypalAdapter(new paypal())));

        facade.processTransaction(mockTx);

        assertEquals(1, txRepo.findAll().size());
        assertSame(mockTx, txRepo.findAll().get(0));
    }

    @Test
    void testProcessTransaction_unregisteredType() {
        Transaction mockTx = mock(Transaction.class);
        when(mockTx.getType()).thenReturn(null);
        when(mockTx.getReciveaccount()).thenReturn(receiver);
        when(mockTx.getAmount()).thenReturn(50.0);

        facade.processTransaction(mockTx);

        assertEquals(0, txRepo.findAll().size());
    }

    @Test
    void testAuditLog_OnRejection() {
        Transaction tx = new Transaction(99999, 1, receiver, sender, TransactionType.TRANSFER);
        ApprovalRequest request = new ApprovalRequest(tx, normalUser, 0000); // PIN خاطئ

        facade.processTransactionWithApproval(tx, request, chain);

        assertEquals(0, txRepo.findAll().size(), "المستودع يجب أن يكون فارغاً عند الرفض");
        assertTrue(facade.commands.isEmpty());
    }
    @Test
    void testProcessTransaction_LegacyBank() {
        Transaction tx = new Transaction(1000, 1, receiver, sender, TransactionType.LEGACY_BANK);

        facade.processTransaction(tx);

        assertFalse(facade.commands.isEmpty());
        assertEquals(1, txRepo.findAll().size());
        assertEquals(TransactionType.LEGACY_BANK, txRepo.findAll().get(0).getType());
    }
    @Test
    void testUndoLast_WhenNoCommandsExist() {
        assertDoesNotThrow(() -> {
            facade.undoLast();
        }, "يجب ألا يرمي استثناء إذا كانت قائمة العمليات فارغة");
    }
    @Test
    void testUndoMultipleTransactions() {
        Transaction tx1 = new Transaction(100, 1, receiver, sender, TransactionType.DISPOSE);
        Transaction tx2 = new Transaction(200, 1, receiver, sender, TransactionType.DISPOSE);

        facade.processTransaction(tx1);
        facade.processTransaction(tx2);
        assertEquals(2, txRepo.findAll().size());

        facade.undoLast();

        assertEquals(1, txRepo.findAll().size());
        assertEquals(tx1, txRepo.findAll().get(0), "يجب أن تبقى المعاملة الأولى فقط");
    }
    @Test
    void testProcessTransaction_Dispose() {
        Account acc = new Account(3, "Test", AccountType.CHECKING, 1000, 1234);
        Transaction tx = new Transaction(500, 1, acc, null, TransactionType.DISPOSE);

        facade.processTransaction(tx);

        assertEquals(1500, acc.getBalance(), "الرصيد يجب أن يزداد بعد عملية الإيداع");
        assertEquals(1, txRepo.findAll().size());
    }

    @Test
    void testProcessTransaction_Transfer() {
        sender.setBalance(1000);
        receiver.setBalance(500);
        Transaction tx = new Transaction(300, 1, receiver, sender, TransactionType.TRANSFER);

        facade.processTransaction(tx);

        assertEquals(694, sender.getBalance());
        assertEquals(800, receiver.getBalance());
        assertEquals(1, txRepo.findAll().size());
    }
    @Test
    void testProcessTransaction_UnknownType_BranchCoverage() {
        Transaction unknownTx = mock(Transaction.class);
        when(unknownTx.getType()).thenReturn(null);

        facade.processTransaction(unknownTx);

        assertEquals(0, txRepo.findAll().size(), "يجب عدم حفظ المعاملة إذا كان النوع مجهولاً");
        assertTrue(facade.commands.isEmpty(), "يجب ألا يضاف أي أمر للقائمة");
    }

    @Test
    void testUndoLast_EmptyList_BranchCoverage() {

        assertDoesNotThrow(() -> facade.undoLast());
    }
}
