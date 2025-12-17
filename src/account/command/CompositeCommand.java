package account.command;

import Transaction.TransactionCommand.TransactionCommand;

import java.util.ArrayList;
import java.util.List;

public class CompositeCommand implements TransactionCommand{

    private List<command> commands = new ArrayList<>();

    public void add(command command) {
        commands.add(command);
    }

    @Override
    public void excute() {
        for (command c : commands) {
            c.excute();
        }
    }

    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

    @Override
    public void redo() {
        excute();
    }
}
