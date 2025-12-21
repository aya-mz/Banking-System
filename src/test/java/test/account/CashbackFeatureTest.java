package test.account;

import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.decorator.CashbackFeature;
import org.junit.jupiter.api.Test;

public class CashbackFeatureTest {

    @Test
    void cashbackIsAddedOnDeposit() {

        Account acc =
                new Account(1, "CashbackAcc", AccountType.SAVING, 1000, 0);

        AccountFeature feature =
                new AccountFeatureAdapter(acc);

        feature = new CashbackFeature(feature);

        feature.deposit(1000); // 1% cashback → +10

        assertEquals(2010, acc.getBalance(), 0.01);
    }
}
