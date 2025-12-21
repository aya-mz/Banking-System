package account.features.recommendation;

import account.features.AccountFeature;

public class BankingRecommendationStrategy implements RecommendationStrategy {

    @Override
    public String recommend(AccountFeature account) {
        if (account.getBalance() > 20000) {
            return "Upgrade to Investment Account";
        }
        return "Standard Saving Account is sufficient";
    }
}


