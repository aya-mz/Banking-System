package account;

import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.decorator.InsuranceFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InsuranceFeatureTest {

    @Test
    void insuranceFeatureAddsDescriptionOnly() {

        Account acc =
                new Account(1, "InsuranceAcc", AccountType.SAVING, 5000, 0);

        AccountFeature base =
                new AccountFeatureAdapter(acc);

        AccountFeature insured =
                new InsuranceFeature(base);

        assertTrue(
                insured.getDescription().contains("Insurance")
        );

        assertEquals(5000, acc.getBalance(), 0.01);
    }
}
