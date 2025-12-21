package account.Accountcommand;

import account.Account;
import account.AccountStateInterface;
import account.states.ClosedState;
import core.Command;

import java.time.LocalDateTime;

public class ChangeState implements Command {

    private Account account;
    private AccountStateInterface oldState;
    private AccountStateInterface newState;

    public ChangeState(Account account, AccountStateInterface newState) {
        this.account = account;
        this.oldState = account.getState();
        this.newState = newState;
    }

    @Override
    public void execute() {
        if (newState.isClosed())
        {
            throw new IllegalArgumentException("the change can't be close try again!");
        }
        account.setState(newState);
        account.setUpdateAt(LocalDateTime.now());
        System.out.println("change state  from "+ oldState.getName()+" to " + newState.getName());

    }

    @Override
    public void undo() {
        account.setState(oldState);
    }

    @Override
    public void redo() {
        execute();
    }
}
