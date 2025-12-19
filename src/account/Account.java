package account;

import java.time.LocalDateTime;

public class Account {
    private int account_id;
    double balance;
    int user_id;
    boolean is_parent;
    int parent_id;
    AccountType type;
    AccountState state;
    String name;
    int Pin_code;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public Account(int user_id, String name, AccountType type, double balance , int parent_id) {
        this.balance = balance;
        this.user_id = user_id;
        this.Pin_code = generatePin();
        this.name = name;
        this.state = AccountState.ACTIVE;
        this.type = type;
        this.parent_id = parent_id;
        this.createdAt = LocalDateTime.now();

    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    private int generatePin() {
        return 1000 + new java.util.Random().nextInt(9000);
    }

    public int getPin_code() {
        return Pin_code;
    }

    public void setPin_code(int pin_code) {
        Pin_code = pin_code;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getParent_id() {
        return parent_id;
    }

    public AccountState getState() {
        return state;
    }


    public void setType(AccountType type) {
        this.type = type;
    }

    public void setState(AccountState state) {
        this.state = state;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + account_id +
                ", balance=" + balance +
                ", user_id=" + user_id +
                ", type=" + type +
                ", state=" + state +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                ", Pin_code=" + Pin_code +
                ", CreateAt =" +createdAt+
                " , UpdateAt = " + updateAt+
                '}';

    }


}
//  ", is_parent=" + is_parent +
