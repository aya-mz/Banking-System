package account;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.decorator.OverdraftProtectionFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OverdraftFeatureTest {

    private Account acc;
    private AccountFeature feature;
    private TransactionEventPublisher publisher;
    private AuditLogObserver audit;
    private NotificationObserver notification;
    private TransactionDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        acc = new Account(1, "TestUser", AccountType.CHECKING, 100, 0);
        feature = new AccountFeatureAdapter(acc);
        feature = new OverdraftProtectionFeature(feature);

        publisher = new TransactionEventPublisher();
        audit = new AuditLogObserver();
        notification = new NotificationObserver();
        publisher.subscribe(audit);
        publisher.subscribe(notification);
        dispatcher = new TransactionDispatcher(publisher);
    }

    @Test
    void withdraw_withinBalance_shouldSucceed() {
        feature.withdraw(50);
        assertEquals(50, acc.getBalance(), 0.01);
    }

    @Test
    void withdraw_upToOverdraftLimit_shouldSucceed() {
        feature.withdraw(600);
        assertEquals(-500, acc.getBalance(), 0.01);
    }

    @Test
    void withdraw_exceedingOverdraft_shouldFail() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> feature.withdraw(700));
        assertEquals("Overdraft limit exceeded", ex.getMessage());
        assertEquals(100, acc.getBalance(), 0.01); // الرصيد لم يتغير
    }

    @Test
    void description_shouldIncludeOverdraftFeature() {
        String desc = feature.getDescription();
        assertTrue(desc.contains("Overdraft Protection"));
    }

    @Test
    void multipleWithdrawals_withinOverdraft_shouldSucceed() {
        feature.withdraw(300);
        feature.withdraw(200);
        assertEquals(-400, acc.getBalance(), 0.01);
    }

    @Test
    void multipleWithdrawals_exceedingOverdraft_shouldFailOnLast() {
        feature.withdraw(400);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> feature.withdraw(400));
        assertEquals("Overdraft limit exceeded", ex.getMessage());
        assertEquals(-300, acc.getBalance(), 0.01);
    }

    @Test
    void withdraw_viaDispatcher_shouldTriggerAuditLog() {
        dispatcher.dispatch(
                () -> feature.withdraw(200),
                "WITHDRAW",
                "Withdraw with overdraft"
        );
        assertEquals(-100, acc.getBalance(), 0.01);
        assertEquals(1, audit.getLogs().size());
    }
}
