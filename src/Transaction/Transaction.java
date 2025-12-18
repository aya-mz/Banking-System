package Transaction;

import account.Account;
import account.AccountType;

import java.time.LocalDateTime;

public class Transaction {
    int transaction_id;

     Account reciveaccount ;
     Account senderaccount ;
    public TransactionType type;
    double amount ;

    int user_id;

    private LocalDateTime createdAt;

    public Transaction(double ammount, int user_id, Account reciveaccount, Account senderaccount, TransactionType type){
      this.user_id = user_id;
        this.reciveaccount = reciveaccount;
        this.amount=ammount;
        this.senderaccount = senderaccount;
        this.createdAt =  LocalDateTime.now();
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public Account getReciveaccount() {
        return reciveaccount;
    }

    public Account getSenderaccount() {
        return senderaccount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public int getTransaction_id() {
        return transaction_id;
    }


}
