package account.features.decorator;

import account.features.AccountFeature;

public class InsuranceFeature extends AccountFeatureDecorator {

    public InsuranceFeature(AccountFeature account) {
        super(account);
    }

    @Override
    public String getDescription() {
        return account.getDescription() + " + Insurance";
    }
}
