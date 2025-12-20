package approval;
import  Transaction.Transaction;
import core.user.User;
public class ApprovalRequest {
    private  Transaction transaction;
    private  User user;
   private int pinCode;
   public ApprovalRequest(Transaction transaction,User user,int pinCode){
       this.transaction=transaction;
       this.user=user;
       this.pinCode=pinCode;
   }
   public  Transaction getTransaction(){
       return  transaction;
   }
   public  User getUser(){
       return  user;
   }
   public  int getPinCode(){
       return  pinCode;
   }
}
