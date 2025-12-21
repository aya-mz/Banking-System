package account.features.strategy;

import account.features.AccountFeature;

public class InterestApplier {

    private InterestStrategy strategy;

    public InterestApplier(InterestStrategy strategy) {
        this.strategy = strategy;
    }

    public void apply(AccountFeature account) {
        double interest = strategy.calculateInterest(account);
        account.deposit(interest);
    }
}
