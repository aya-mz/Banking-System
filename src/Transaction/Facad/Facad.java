package Transaction.Facad;

import Transaction.*;
import Transaction.Adapter.*;
import Transaction.TransactionCommand.*;
import approval.ApprovalHandler;
import approval.ApprovalRequest;
import core.Command;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

public class Facad {

    public List<Command> commands = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Facad.class.getName());


    private final Map<TransactionType, Function<Transaction, Command>> commandMap = new HashMap<>();

    public Facad() {

        commandMap.put(TransactionType.PAPAL,
                t -> new payment(t, new paypalAdapter(new paypal())));
        commandMap.put(TransactionType.LEGACY_BANK,
                t -> new payment(t, new LegacyAdapter(new legacyBankSystem())));
        commandMap.put(TransactionType.DISPOSE,
                Dispose::new);
        commandMap.put(TransactionType.TRANSFER ,
                 transfers::new);
        commandMap.put(TransactionType.withdrawals,
                  withdrawals::new);
    }


    public void processTransaction(Transaction transaction) {
        Function<Transaction, Command> commandFactory = commandMap.get(transaction.getType());
        if (commandFactory != null) {
            Command command = commandFactory.apply(transaction);
            command.execute();
            commands.add(command);
            logger.info("Transaction processed: " + transaction.getType());
        } else {
            logger.warning("No command registered for transaction type: " + transaction.getType());
        }
    }

    public void undoLast() {
        if (!commands.isEmpty()) {
            Command last = commands.remove(commands.size() - 1);
            last.undo();
        }
    }

    public void printOperations() {
        commands.forEach(cmd -> System.out.println(cmd.getClass().getSimpleName()));
    }

    public void processTransactionWithApproval(Transaction transaction, ApprovalRequest request, ApprovalHandler chain) {
        if (!chain.approve(request)) {
            logger.warning("Transaction rejected by approval chain: " + transaction.getType());
            return;
        }

        processTransaction(transaction);
    }

    public void registerTransactionType(TransactionType type, Function<Transaction, Command> factory) {
        commandMap.put(type, factory);
    }
}
