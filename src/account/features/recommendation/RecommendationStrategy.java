package account.features.recommendation;

import account.features.AccountFeature;

public interface RecommendationStrategy {
    String recommend(AccountFeature account);
}
