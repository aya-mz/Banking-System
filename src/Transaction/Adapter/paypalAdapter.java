package Transaction.Adapter;

public class paypalAdapter implements paymantUi {
    paypal paypal ;

    public paypalAdapter(paypal paypal) {
        this.paypal =paypal;
    }

    @Override
    public void Paymoney(double amount) {
        paypal.makepayment(amount);
    }
}
