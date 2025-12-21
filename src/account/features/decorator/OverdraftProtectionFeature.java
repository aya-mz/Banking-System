package account.features.decorator;

import account.features.AccountFeature;

public class OverdraftProtectionFeature extends AccountFeatureDecorator {

    private final double overdraftLimit = 500;

    public OverdraftProtectionFeature(AccountFeature account) {
        super(account);
    }

    @Override
    public void withdraw(double amount) {
        if (account.getBalance() + overdraftLimit < amount) {
            throw new IllegalArgumentException("Overdraft limit exceeded");
        }
        account.withdraw(amount);
    }

    @Override
    public String getDescription() {
        return account.getDescription() + " + Overdraft Protection";
    }
}

