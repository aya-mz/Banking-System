package Transaction.TransactionCommand;

import account.Accountcommand.Command;
import Transaction.Transaction;
public interface TransactionCommand extends Command {
    Transaction getTransaction();
}
