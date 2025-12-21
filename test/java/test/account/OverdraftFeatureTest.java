package account;

import account.Account;
import account.AccountType;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.decorator.OverdraftProtectionFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverdraftFeatureTest {
    @Test
    void overdraftAllowsNegativeBalanceWithinLimit() {

        Account acc = new Account(1,"Test", AccountType.CHECKING,100,0);
        AccountFeature feature = new AccountFeatureAdapter(acc);

        feature = new OverdraftProtectionFeature(feature);

        feature.withdraw(500);

        assertEquals(-400, acc.getBalance(), 0.01);
    }

}
