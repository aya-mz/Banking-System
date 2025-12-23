package approvalChains.test;
import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.TransactionEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import core.user.*;
import Transaction.*;
import account.*;
import approval.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class ApprovalChainTest {
    private Account sender;
    private Account receiver;
    private User adminUser;
    private User normalUser;
    private ApprovalHandler chain;

    TransactionEventPublisher publisher = new TransactionEventPublisher();
    AuditLogObserver audit = new AuditLogObserver();
    TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

    @BeforeEach
    void setup() {
        publisher.subscribe(audit);

        sender = new Account(1, "Sender", AccountType.CHECKING, 15000, 0);
        receiver = new Account(2, "Receiver", AccountType.SAVING, 1000, 0);

        adminUser = new User(1, "admin@test.com", Role.ADMIN,
                new AuthInfo("admin123"), null);

        normalUser = new User(2, "user@test.com", Role.CUSTOMER,
                new AuthInfo("user123"), null);


        ApprovalHandler teller = new TellerHandler();
        ApprovalHandler manager = new ManagerHandler();
        ApprovalHandler admin = new AdminHandler();

        teller.setNext(manager);
        manager.setNext(admin);

        chain = teller;
    }

    @Test
    void transaction_withValidData_shouldPass() {
        Transaction tx = new Transaction(1000, 1, receiver, sender, TransactionType.TRANSFER);
        ApprovalRequest request = new ApprovalRequest(tx, normalUser, sender.getPin_code());
        AtomicBoolean result = new AtomicBoolean(false);
        dispatcher.dispatch(
                () -> result.set(chain.approve(request)),
                "APPROVAL_CHECK",
                "Approval validation executed"
        );
        assertTrue(result.get());
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void transaction_withWrongPin_shouldFail() {
        Transaction tx = new Transaction(1000, 1, receiver, sender, TransactionType.TRANSFER);
        ApprovalRequest request = new ApprovalRequest(tx, normalUser, 9999);
        AtomicBoolean result = new AtomicBoolean(false);

        dispatcher.dispatch(
                () -> result.set(chain.approve(request)),
                "APPROVAL_CHECK",
                "Approval failed: wrong PIN"
        );

        assertFalse(result.get());
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void transaction_withInsufficientBalance_shouldFail() {
        Transaction tx = new Transaction(20000, 1, receiver, sender, TransactionType.TRANSFER);
        ApprovalRequest request =
                new ApprovalRequest(tx, normalUser, sender.getPin_code());

        assertFalse(chain.approve(request));
    }

    @Test
    void largeTransaction_withoutAdmin_shouldFail() {
        Transaction tx = new Transaction(15000, 1, receiver, sender, TransactionType.TRANSFER);
        ApprovalRequest request = new ApprovalRequest(tx, normalUser, sender.getPin_code());

        AtomicBoolean result = new AtomicBoolean(true);

        dispatcher.dispatch(
                () -> result.set(chain.approve(request)),
                "APPROVAL_CHECK",
                "Approval failed: insufficient balance"
        );

        assertFalse(result.get());
        assertEquals(1, audit.getLogs().size());    }

    @Test
    void largeTransaction_withAdmin_shouldPass() {
        Transaction tx = new Transaction(15000, 1, receiver, sender, TransactionType.TRANSFER);
        ApprovalRequest request = new ApprovalRequest(tx, adminUser, sender.getPin_code());

        AtomicBoolean result = new AtomicBoolean(false);

        dispatcher.dispatch(
                () -> result.set(chain.approve(request)),
                "APPROVAL_CHECK",
                "Admin approval granted"
        );

        assertTrue(result.get());
        assertEquals(1, audit.getLogs().size());    }
}
