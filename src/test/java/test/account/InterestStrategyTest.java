package account;

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

        Account acc = new Account(1,"Test",AccountType.SAVING,1000,0);
        AccountFeature feature = new AccountFeatureAdapter(acc);

        InterestStrategy strategy = new SavingInterestStrategy();
        InterestApplier scheduler = new InterestApplier(strategy);

        scheduler.apply(feature);

        assertEquals(1030, acc.getBalance(), 0.01);
    }
}
