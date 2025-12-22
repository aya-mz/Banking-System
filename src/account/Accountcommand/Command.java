package account.Accountcommand;

public interface Command {
    void execute();
    void redo();
    void undo();


}
