package account.features;

import account.Account;

public class AccountFeatureAdapter implements AccountFeature {

    private Account account;

    public AccountFeatureAdapter(Account account) {
        this.account = account;
    }

    @Override
    public double getBalance() {
        return account.getBalance();
    }

    @Override
    public void deposit(double amount) {
        account.setBalance(getBalance()+amount);
    }

    @Override
    public void withdraw(double amount) {
        account.setBalance(getBalance()-amount);
    }

    @Override
    public String getDescription() {
        return "Account [" + account.getName() + "] type=" + account.getType();
    }
}
