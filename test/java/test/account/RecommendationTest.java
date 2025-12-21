package account;

import account.Account;
import account.AccountType;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.recommendation.BankingRecommendationStrategy;
import account.features.recommendation.RecommendationStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommendationTest {
    @Test
    void recommendationWorks() {

        Account acc = new Account(1,"VIP", AccountType.SAVING,30000,0);
        AccountFeature feature = new AccountFeatureAdapter(acc);

        RecommendationStrategy strategy = new BankingRecommendationStrategy();

        assertEquals(
                "Upgrade to Investment Account",
                strategy.recommend(feature)
        );
    }

}
