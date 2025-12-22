package Transaction.Facad;

import Transaction.*;
import Transaction.Adapter.*;
import Transaction.Facad.Facad;
import Transaction.TransactionCommand.*;
import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.AccountType;
import approval.*;
import core.Command;
import account.Account;

import core.user.AuthInfo;
import core.user.Role;
import core.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FacadTest {
    Account sender;
    Account receiver;
    User adminUser;
    ApprovalHandler chain;
    User normalUser;
    Facad facade;

    @Mock
    paypalAdapter mockPaypalAdapter;

    @Mock
    LegacyAdapter mockLegacyAdapter;

    @Mock
    Transaction mockTransaction;

    @Mock
    Account mockAccount;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        facade = new Facad();


        when(mockTransaction.getReciveaccount()).thenReturn(mockAccount);
        when(mockTransaction.getAmount()).thenReturn(100.0);
        when(mockAccount.getBalance()).thenReturn(500.0);

            MockitoAnnotations.openMocks(this);
            facade = new Facad();

            // Mock الحساب والمعاملة
            sender = new Account(1, "Sender", AccountType.CHECKING, 15000, 1234);
            receiver = new Account(2, "Receiver", AccountType.SAVING, 1000, 0);

            adminUser = new User("1", "admin@test.com", Role.ADMIN, new AuthInfo("admin123"), null);

            // إنشاء Approval Chain
            ApprovalHandler teller = new TellerHandler();
            ApprovalHandler manager = new ManagerHandler();
            ApprovalHandler admin = new AdminHandler();

            teller.setNext(manager);
            manager.setNext(admin);

            chain = teller;

            // Mock Transaction
            when(mockTransaction.getReciveaccount()).thenReturn(mockAccount);
            when(mockTransaction.getAmount()).thenReturn(100.0);
            when(mockAccount.getBalance()).thenReturn(500.0);


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
        verify(mockAccount, times(1)).setBalance(400.0);
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void testProcessTransactionSuccess_Legacy() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();

        NotificationObserver notification = new NotificationObserver();

        publisher.subscribe(audit);
        publisher.subscribe(notification);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        when(mockTransaction.getType()).thenReturn(TransactionType.LEGACY_BANK);

        dispatcher.dispatch(
                () -> facade.processTransaction(mockTransaction),
                "PROCESS_TRANSACTION",
                "Legacy transaction processed"
        );

        verify(mockAccount, times(1)).getBalance();
        verify(mockAccount, times(1)).setBalance(400.0);
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void testProcessTransactionFailure_UnregisteredType() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        when(mockTransaction.getType()).thenReturn(null);

        dispatcher.dispatch(
                () -> facade.processTransaction(mockTransaction),
                "PROCESS_TRANSACTION_FAILED",
                "Unregistered transaction type"
        );

        assertTrue(facade.commands.isEmpty());
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void testUndoLast() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        when(mockTransaction.getType()).thenReturn(TransactionType.PAPAL);

        dispatcher.dispatch(
                () -> {
                    facade.processTransaction(mockTransaction);
                    facade.undoLast();
                },
                "UNDO_TRANSACTION",
                "Last transaction undone"
        );

        verify(mockAccount, times(2)).setBalance(anyDouble());
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void testRegisterNewTransactionType() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();

        NotificationObserver notification = new NotificationObserver();

        publisher.subscribe(audit);
        publisher.subscribe(notification);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        TransactionType NEW_TYPE = TransactionType.valueOf("VISA");
        Transaction newTransaction = mock(Transaction.class);
        Account newAccount = mock(Account.class);

        when(newTransaction.getType()).thenReturn(NEW_TYPE);
        when(newTransaction.getReciveaccount()).thenReturn(newAccount);
        when(newTransaction.getAmount()).thenReturn(50.0);
        when(newAccount.getBalance()).thenReturn(100.0);

        dispatcher.dispatch(
                () -> {
                    facade.registerTransactionType(
                            NEW_TYPE,
                            t -> new payment(t, new paypalAdapter(new paypal()))
                    );
                    facade.processTransaction(newTransaction);
                },
                "REGISTER_TRANSACTION_TYPE",
                "New transaction type registered and executed"
        );

        verify(newAccount, times(1)).setBalance(50.0);
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void approvedTransaction_shouldExecuteFacade() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();

        NotificationObserver notification = new NotificationObserver();

        publisher.subscribe(audit);
        publisher.subscribe(notification);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        Transaction tx = new Transaction(500, 1, receiver, sender, TransactionType.PAPAL);
        ApprovalRequest request = new ApprovalRequest(tx, adminUser, sender.getPin_code());

        dispatcher.dispatch(
                () -> facade.processTransactionWithApproval(tx, request, chain),
                "APPROVED_TRANSACTION",
                "Transaction approved and executed"
        );

        assertFalse(facade.commands.isEmpty());
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void rejectedTransaction_shouldNotExecuteFacade() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        Transaction tx = new Transaction(500, 1, receiver, sender, TransactionType.PAPAL);

        ApprovalRequest request = new ApprovalRequest(tx, normalUser, 9999);

        dispatcher.dispatch(
                () -> facade.processTransactionWithApproval(tx, request, chain),
                "REJECTED_TRANSACTION",
                "Transaction rejected by approval chain"
        );

        assertTrue(facade.commands.isEmpty());
        assertEquals(1, audit.getLogs().size());
    }
}
