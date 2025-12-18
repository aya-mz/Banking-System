package account.Accountcommand;

import core.Command;

import java.util.ArrayList;
import java.util.List;

public class CompositeCommand implements Command {

    private List<Command> commands = new ArrayList<>();

    public void add(Command command) {
        commands.add(command);
    }

    @Override
    public void execute() {
        for (Command c : commands) {
            c.execute();
        }
    }

    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) commands.get(i).undo();
    }

    @Override
    public void redo() {
        execute();
    }
}
