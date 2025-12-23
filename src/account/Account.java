package account;

import account.states.ActiveState;
import java.time.LocalDateTime;


public class Account {
    private int account_id;
    double balance;
    int user_id;
    boolean is_parent;
    int parent_id;
    AccountType type;
    AccountStateInterface state;
    String name;
    int Pin_code;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public Account(int user_id, String name, AccountType type, double balance, int parent_id) {
        this.balance = balance;
        this.user_id = user_id;
        this.Pin_code = generatePin();
        this.name = name;
        this.state = ActiveState.getInstance();
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

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public AccountStateInterface getState() {
        return state;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setState(AccountStateInterface state) {
        this.state = state;
        this.updateAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public int getUser_id() {
        return user_id;
    }



    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + account_id +
                ", balance=" + balance +
                ", user_id=" + user_id +
                ", type=" + type +
                ", state=" +  state.getName() +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                ", Pin_code=" + Pin_code +
                ", CreateAt=" + createdAt +
                ", UpdateAt=" + updateAt +
                '}';
    }
}
