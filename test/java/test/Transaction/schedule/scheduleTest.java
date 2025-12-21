package Transaction.schedule;

import account.Account;
import account.AccountType;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.strategy.CheckingInterestStrategy;
import account.features.strategy.InterestStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class scheduleTest {
    @Test
    void monthlyInterestShouldBeApplied() {

        Account baseAccount = new Account(1,"VIP", AccountType.SAVING,30000,0);
        AccountFeature account = new AccountFeatureAdapter(baseAccount);

        InterestStrategy strategy = new CheckingInterestStrategy();

        MonthlyInterestTransaction tx = new MonthlyInterestTransaction(account, strategy);

        TransactionScheduler scheduler = new TransactionScheduler();
        scheduler.add(tx);

        scheduler.run(LocalDate.now());

        assertTrue(account.getBalance() > 1000);
    }

    @Test
    void scheduledInterestTransactionShouldExecute() {

        Account account = new Account(1,"VIP", AccountType.SAVING,30000,0);
        AccountFeature feature = new AccountFeatureAdapter(account);

        InterestStrategy strategy = new CheckingInterestStrategy();

        ScheduledTransaction transaction = new MonthlyInterestTransaction(feature, strategy);

        TransactionScheduler scheduler = new TransactionScheduler();
        scheduler.add(transaction);

        double before = feature.getBalance();

        scheduler.run(LocalDate.now());

        double after = feature.getBalance();
        assertTrue(after > before);
    }
}
