package Transaction.schedule;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.Account;
import account.AccountType;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.strategy.CheckingInterestStrategy;
import account.features.strategy.InterestStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class scheduleTest {
    @Test
    void monthlyInterestShouldBeApplied() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);
        Account baseAccount = new Account(1,"VIP", AccountType.SAVING,30000,0);
        AccountFeature account = new AccountFeatureAdapter(baseAccount);

        InterestStrategy strategy = new CheckingInterestStrategy();

        MonthlyInterestTransaction tx = new MonthlyInterestTransaction(account, strategy);

        TransactionScheduler scheduler = new TransactionScheduler();
        scheduler.add(tx);

        dispatcher.dispatch(
                () -> scheduler.run(LocalDate.now()),
                "SCHEDULED_INTEREST",
                "Monthly interest executed"
        );
        assertTrue(account.getBalance() > 1000);
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void scheduledInterestTransactionShouldExecute() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);
        Account account = new Account(1,"VIP", AccountType.SAVING,30000,0);
        AccountFeature feature = new AccountFeatureAdapter(account);

        InterestStrategy strategy = new CheckingInterestStrategy();

        ScheduledTransaction transaction = new MonthlyInterestTransaction(feature, strategy);

        TransactionScheduler scheduler = new TransactionScheduler();
        scheduler.add(transaction);

        double before = feature.getBalance();

        dispatcher.dispatch(
                () -> scheduler.run(LocalDate.now()),
                "SCHEDULED_INTEREST",
                "Monthly interest executed"
        );
        double after = feature.getBalance();
        assertTrue(after > before);
        assertEquals(1, audit.getLogs().size());
    }
}
