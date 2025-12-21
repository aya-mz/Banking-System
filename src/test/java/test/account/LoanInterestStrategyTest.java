package account;

import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.strategy.InterestApplier;
import account.features.strategy.LoanInterestStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoanInterestStrategyTest {

    @Test
    void loanInterestIsAppliedAsDebtIncrease() {

        Account loanAccount =
                new Account(1, "LoanAcc", AccountType.LOAN, 1000, 0);

        AccountFeature feature =
                new AccountFeatureAdapter(loanAccount);

        LoanInterestStrategy strategy =
                new LoanInterestStrategy();

        InterestApplier scheduler =
                new InterestApplier(strategy);

        scheduler.apply(feature);

        assertEquals(950, loanAccount.getBalance(), 0.01);
    }
}
