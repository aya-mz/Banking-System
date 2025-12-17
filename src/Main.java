import account.Account;
import account.AccountState;
import account.AccountType;
import account.command.*;
import account.inmemmory;
import core.user.UserTest;

public class Main {
    public static void main(String[] args) {
        UserTest userTest=new UserTest();
        userTest.test1();
        inmemmory repo = new inmemmory();

        Account account1 = new Account(0, "Magda", AccountType.CHECKING, 2000 , 0 );


        Craete create1 = new Craete(account1, repo);
        create1.excute();
        Account account2 = new Account(1, "Lana", AccountType.SAVING, 4000 ,0 );

        Craete create2 = new Craete(account2, repo);
        create2.excute();

        System.out.println(account1);
        System.out.println(account2);
        ChangePinCode change1 = new ChangePinCode(account1, 1234);
        ChangePinCode change2 = new ChangePinCode(account2, 2225);

        ChangeState state1 = new ChangeState(account1, AccountState.FROZEN);
        ChangeType type = new ChangeType(account2, AccountType.LOAN);
        Close close = new Close(account1 , repo);

        CompositeCommand command = new CompositeCommand();
        command.add(change1);
        command.add(change2);
        command.add(state1);
        command.add(type);


        command.excute();

        System.out.println("after change State ");
        System.out.println(account1);
        System.out.println(account2);


    }
}