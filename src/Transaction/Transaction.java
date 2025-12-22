package Transaction;

import account.Account;
import account.AccountType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    //    public List<String> toReportData() {
//        List<String> summary = new ArrayList<>();
//        summary.add(
//                "ID: " + transaction_id +
//                        ", Type: " + type +
//                        ", Amount: " + amount +
//                        ", Sender: " + senderaccount.getAccount_id() +
//                        ", Receiver: " + reciveaccount.getAccount_id() +
//                        ", Date: " + createdAt
//        );
//        return summary;
//    }

}
