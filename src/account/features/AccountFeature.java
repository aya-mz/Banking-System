package account.features;

public interface AccountFeature {

    double getBalance();

    void deposit(double amount);

    void withdraw(double amount);

    String getDescription();
}
