package Transaction.Facad;

import Transaction.*;
import Transaction.Adapter.*;
import Transaction.TransactionCommand.*;
import approval.*;
import account.Accountcommand.Command;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

public class Facad {

    public List<TransactionCommand> commands = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Facad.class.getName());
    private TransactionRepository transactionRepository;
    private final Map<TransactionType, Function<Transaction, TransactionCommand>> commandMap = new HashMap<>();

    public Facad(TransactionRepository txRepo) {
        this.transactionRepository = txRepo;
        initCommandMap();
    }

    private void initCommandMap() {
        commandMap.put(TransactionType.PAPAL,
                t -> new payment(t, new paypalAdapter(new paypal())));
        commandMap.put(TransactionType.LEGACY_BANK,
                t -> new payment(t, new LegacyAdapter(new legacyBankSystem())));
        commandMap.put(TransactionType.DISPOSE,
                Dispose::new);
        commandMap.put(TransactionType.TRANSFER,
                transfers::new);
        commandMap.put(TransactionType.withdrawals,
                withdrawals::new);
    }

    public void processTransaction(Transaction transaction) {
        Function<Transaction, TransactionCommand> commandFactory = commandMap.get(transaction.getType());
        if (commandFactory != null) {
            TransactionCommand command = commandFactory.apply(transaction);
            command.execute();
            transactionRepository.save(transaction);
            commands.add(command);
            logger.info("Transaction processed and saved: " + transaction.getType() + " ID: " + transaction.getTransaction_id());
        } else {
            logger.warning("No command registered for transaction type: " + transaction.getType());
        }
    }

    public void undoLast() {
        if (!commands.isEmpty()) {
            TransactionCommand last = commands.remove(commands.size() - 1);
            last.undo();

            // حذف المعاملة من Repository إن كان Command يدعم Transaction
            if (last instanceof TransactionCommand) {
                transactionRepository.delete(((TransactionCommand) last).getTransaction());
            }
        }
    }

    public void processTransactionWithApproval(Transaction transaction, ApprovalRequest request, ApprovalHandler chain) {
        if (!chain.approve(request)) {
            logger.warning("Transaction rejected by approval chain: " + transaction.getType());
            return;
        }
        processTransaction(transaction);
    }

    public void registerTransactionType(TransactionType type, Function<Transaction, TransactionCommand> factory) {
        commandMap.put(type, factory);
    }

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }
}
