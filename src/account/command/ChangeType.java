package account.command;

import account.Account;
import account.AccountType;

import java.time.LocalDateTime;

public class ChangeType implements command {

    private Account account;
    private AccountType oldType;
    private AccountType newType;

    public ChangeType(Account account, AccountType newType) {
        this.account = account;
        this.oldType = account.getType();
        this.newType = newType;
    }

    @Override
    public void excute() {
        account.setType(newType);
        account.setUpdateAt(LocalDateTime.now());
        System.out.println("change type success you change type from "+ oldType+" to " + newType);
    }

    @Override
    public void undo() {
        account.setType(oldType);
    }

    @Override
    public void redo() {
        excute();
    }


}
