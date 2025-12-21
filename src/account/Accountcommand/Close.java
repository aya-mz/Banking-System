package account.Accountcommand;

import account.Account;
import account.AccountState;
import account.AccountType;
import account.inmemmory;
import core.Command;

public class Craete  implements Command {
    Account account;
    inmemmory inmemmory;
    public Craete(Account account  , inmemmory inmemmory ){
        this.account = account;
        this.inmemmory = inmemmory;
    }

    @Override
    public void execute() {
        createvalidation();
        inmemmory.save(account);
        System.out.println("the account create successfully ! the pin code for your account is " +account.getPin_code());
    }

    @Override
    public void redo() {
        inmemmory.save(account);

    }

    @Override
    public void undo() {
        inmemmory.delete(account.getAccount_id());

    }

    void createvalidation(){
        if(account.getName() == null || account.getName().isEmpty()){
            throw new IllegalArgumentException("the name is required!");
        }
        if(null == account.getType()){
            throw new IllegalArgumentException("the type is required!");
        }
        // AccountGroup يمكن أن يكون رصيده 0 (المجموعات تبدأ بصفر)
        boolean isGroup = account instanceof account.AccountGroup;
        if(account.getBalance() < 100 && account.getType() != AccountType.LOAN && !isGroup){
            throw new IllegalArgumentException("the balance should be 100 or more !");
        }
        if(account.getBalance() < 0){
            throw new IllegalArgumentException("the balance should be 0 or more !");
        }
        if (account.getParent_id() != 0) {
            Account parent = inmemmory.findById(account.getParent_id());
            if (parent == null) {
                throw new IllegalArgumentException("Parent account not found");
            }
            if (parent.getState().isClosed()) {
                throw new IllegalArgumentException("Parent account is closed");
            }}
    }
}
