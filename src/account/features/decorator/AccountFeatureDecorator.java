package account.features.decorator;

import account.features.AccountFeature;

public abstract class AccountFeatureDecorator implements AccountFeature {

    protected AccountFeature account;

    public AccountFeatureDecorator(AccountFeature account) {
        this.account = account;
    }

    public double getBalance() {
        return account.getBalance();
    }

    public void deposit(double amount) {
        account.deposit(amount);
    }

    public void withdraw(double amount) {
        account.withdraw(amount);
    }
}
