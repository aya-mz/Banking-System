package Transaction.TransactionCommand;

import Transaction.Transaction;
import account.Accountcommand.Command;

import java.util.logging.Logger;

public class withdrawals implements TransactionCommand {
    Transaction transaction ;
    private static final Logger logger =
            Logger.getLogger(withdrawals.class.getName());
    double oldbalance ;
    public withdrawals( Transaction transaction){
        this.transaction = transaction;
    }
    // sender account use
    @Override
    public void execute() {
   oldbalance =  transaction.getSenderaccount().getBalance();
   transaction.getSenderaccount().setBalance(oldbalance - transaction.getAmount());


        logger.info("Withdraw executed: amount=" + transaction.getAmount());

    }

    @Override
    public void undo() {
     transaction.getSenderaccount().setBalance(oldbalance);
    }

    @Override
    public void redo() {
      execute();
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
