package account.command;

import account.Account;
import account.AccountState;
import account.AccountType;
import account.inmemmory;

import java.time.LocalDateTime;

public class Close implements command{
Account account ;
inmemmory inmemmory;
    public Close(Account account , inmemmory inmemmory){
        this.account=account;
        this.inmemmory=inmemmory;
    }
    @Override
    public void excute() {
        closevalidation();
        account.setState(AccountState.CLOSE);
        account.setUpdateAt(LocalDateTime.now());
        System.out.println("Account closed successfully");
    }

    @Override
    public void redo() {
        throw new IllegalArgumentException("cant redo close!");
    }

    @Override
    public void undo() {
        throw new IllegalArgumentException("cant undo close!");
    }

 void  closevalidation(){
        if(account.getBalance()>0 && account.getType() != AccountType.LOAN){
            throw new IllegalArgumentException("you should dispose "+account.getBalance()+" first!");
        }
     var children = inmemmory.findAllChildren(account.getAccount_id());
     if (!children.isEmpty()) {


         for (Account child : children) {
             if (child.getState() == AccountState.ACTIVE) {
                 throw new IllegalArgumentException(
                         "cant close account while child account "
                                 + child.getAccount_id()
                                 + " is ACTIVE"
                 );
             }
         }


         throw new IllegalArgumentException(
                 "cant close account with child accounts"
         );}

    }
}
