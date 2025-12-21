package account.features.strategy;

import account.features.AccountFeature;

public class LoanInterestStrategy implements InterestStrategy {
    @Override
    public double calculateInterest(AccountFeature account) {
        return account.getBalance() * -0.05;
    }
}

