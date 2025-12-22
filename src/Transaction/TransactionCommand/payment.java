package Transaction.TransactionCommand;

import Transaction.Adapter.paymantUi;
import Transaction.Transaction;
import account.Accountcommand.Command;

import java.util.logging.Logger;

public class payment implements TransactionCommand {

    private Transaction transaction;
    private paymantUi paymentSystem;
    private double oldBalance;
    private static final Logger logger =
                Logger.getLogger(paymantUi.class.getName());
    public payment(Transaction transaction, paymantUi paymentSystem) {
        this.transaction = transaction;
        this.paymentSystem = paymentSystem; }
    @Override
    public void execute() {
        oldBalance = transaction.getReciveaccount().getBalance();
        transaction.getReciveaccount().setBalance(oldBalance - transaction.getAmount());
        paymentSystem.Paymoney(transaction.getAmount());
        logger.info("Payment executed: amount=" + transaction.getAmount() + " via " + paymentSystem.getClass().getSimpleName());
    }

    @Override public void undo() {
        transaction.getReciveaccount().setBalance(oldBalance);
        logger.info("Payment undone: amount=" + transaction.getAmount());
    }


    @Override public void redo() {
        execute();
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
