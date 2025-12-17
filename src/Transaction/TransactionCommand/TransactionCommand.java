package Transaction.TransactionCommand;

public interface TransactionCommand {
    void excute();
    void undo();
    void redo();
}
