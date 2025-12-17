package Transaction;

import account.Account;

import java.time.LocalDateTime;

public class Transaction {
    int transaction_id;

    Account account ;
    double amount ;
    int re_account_id;
    int teller_id ;

    private LocalDateTime createdAt;

    Transaction(double ammount , int re_account_id , int teller_id , Account account){
      this.teller_id = teller_id;
        this.account = account;
        this.amount=ammount;
        this.re_account_id = re_account_id;
        this.createdAt =  LocalDateTime.now();
    }

    public double getAmount() {
        return amount;
    }

    public int getRe_account_id() {
        return re_account_id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
