package account.Accountcommand;

import account.Account;
import account.AccountState;
import core.Command;

import java.time.LocalDateTime;

public class ChangeState implements Command {

    private Account account;
    private AccountState oldState;
    private AccountState newState;

    public ChangeState(Account account, AccountState newState) {
        this.account = account;
        this.oldState = account.getState();
        this.newState = newState;
    }

    @Override
    public void execute() {
        if (newState == AccountState.CLOSE)
        {
            throw new IllegalArgumentException("the change can't be close try again!");
        }
        account.setState(newState);
        account.setUpdateAt(LocalDateTime.now());
        System.out.println("change state  from "+ oldState+" to " + newState);

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
