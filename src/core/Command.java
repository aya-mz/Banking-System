package core;

public interface Command {
    void execute();
    void redo();
    void undo();


}
