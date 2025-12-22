package account;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.strategy.InterestStrategy;
import account.features.strategy.SavingInterestStrategy;
import account.features.strategy.InterestApplier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterestStrategyTest {
    @Test
    void savingInterestAppliedCorrectly() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);
        Account acc = new Account(1,"Test",AccountType.SAVING,1000,0);
        AccountFeature feature = new AccountFeatureAdapter(acc);

        InterestStrategy strategy = new SavingInterestStrategy();
        InterestApplier scheduler = new InterestApplier(strategy);

        dispatcher.dispatch(
                () -> scheduler.apply(feature),
                "INTEREST_APPLIED",
                "Saving interest applied"
        );
        assertEquals(1030, acc.getBalance(), 0.01);
        assertEquals(1, audit.getLogs().size());
    }
}
