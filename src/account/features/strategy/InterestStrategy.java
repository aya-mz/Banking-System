package account.features.strategy;

import account.features.AccountFeature;

public interface InterestStrategy {
    double calculateInterest(AccountFeature account);
}
