package Transaction.TransactionCommand;

import Transaction.Transaction;
import core.Command;

import java.util.logging.Logger;


public class transfers implements Command {

    private Transaction transaction;
    private double senderOldBalance;
    private double receiverOldBalance;
    private static final Logger logger =
            Logger.getLogger(transfers.class.getName());
    public transfers(Transaction transaction) {
        this.transaction = transaction;
    }
    double fee;
    @Override
    public void execute() {
        double amount = transaction.getAmount();
    fee   = amount * 0.02;

        senderOldBalance = transaction.getSenderaccount().getBalance();
        receiverOldBalance = transaction.getReciveaccount().getBalance();

        transaction.getSenderaccount()
                .setBalance(senderOldBalance - (amount + fee));

        transaction.getReciveaccount()
                .setBalance(receiverOldBalance + amount);


        logger.info("transfer executed: amount=" + transaction.getAmount());
    }


    @Override
    public void undo() {
        transaction.getSenderaccount().setBalance(senderOldBalance);
        transaction.getReciveaccount().setBalance(receiverOldBalance);
    }

    @Override
    public void redo() {
        execute();
    }
}
