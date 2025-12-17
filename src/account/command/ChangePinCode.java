package account.command;

import account.Account;

import java.time.LocalDateTime;

public class ChangePinCode implements command {

    private Account account;
    private int oldPin;
    private int newPin;

    public ChangePinCode(Account account, int newPin) {
        this.account = account;
        this.oldPin = account.getPin_code();
        this.newPin = newPin;
    }

    @Override
    public void excute() {
        account.setPin_code(newPin);
        account.setUpdateAt(LocalDateTime.now());
        System.out.println("change  pin from "+ oldPin+"to " + newPin);

    }

    @Override
    public void undo() {
        account.setPin_code(oldPin);
    }

    @Override
    public void redo() {
        excute();
    }
}
