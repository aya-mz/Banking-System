package account;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.decorator.InsuranceFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InsuranceFeatureTest {

    @Test
    void insuranceFeatureAddsDescriptionOnly() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);
        Account acc = new Account(1, "InsuranceAcc", AccountType.SAVING, 5000, 0);

        AccountFeature base = new AccountFeatureAdapter(acc);

        AccountFeature insured = new InsuranceFeature(base);
        dispatcher.dispatch(
                () -> insured.getDescription(),
                "FEATURE_ADDED",
                "Insurance feature added"
        );
        assertTrue(insured.getDescription().contains("Insurance"));

        assertEquals(5000, acc.getBalance(), 0.01);
        assertEquals(1, audit.getLogs().size());
    }
}
