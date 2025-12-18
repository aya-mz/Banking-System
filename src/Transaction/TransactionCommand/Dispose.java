package Transaction.TransactionCommand;

import Transaction.Transaction;
import core.Command;

import java.util.logging.Logger;

public class Dispose implements Command {

    public  Dispose ( Transaction transaction ){
        this.transaction = transaction;
    }
    Transaction transaction ;
     double oldbalance ;
    private static final Logger logger =
            Logger.getLogger(Dispose.class.getName());
// receiver account use
    @Override
    public void execute() {
        oldbalance  =  transaction.getReciveaccount().getBalance();
        transaction.getReciveaccount().setBalance(oldbalance + transaction.getAmount());
        logger.info("dispose executed: amount=" + transaction.getAmount());

    }

    @Override
    public void undo() {
      transaction.getReciveaccount().setBalance(oldbalance);
    }

    @Override
    public void redo() {
      execute();
    }
}
