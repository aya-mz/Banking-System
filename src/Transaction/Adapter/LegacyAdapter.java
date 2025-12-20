package Transaction.Adapter;

public class LegacyAdapter implements paymantUi {
    legacyBankSystem legacyBankSystem ;
    public  LegacyAdapter(legacyBankSystem legacyBankSystem){
        this.legacyBankSystem = legacyBankSystem;
    }
    @Override
    public void Paymoney(double amount) {
        legacyBankSystem.MakePay(amount);
    }
}
