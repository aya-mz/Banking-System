package account.features.decorator;

import account.features.AccountFeature;

public class CashbackFeature extends AccountFeatureDecorator {

    public CashbackFeature(AccountFeature account) {
        super(account);
    }

    @Override
    public void deposit(double amount) {
        double cashback = amount * 0.01;
        account.deposit(amount + cashback);
    }

    @Override
    public String getDescription() {
        return account.getDescription() + " + Cashback";
    }
}

