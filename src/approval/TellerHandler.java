package approval;

import account.AccountState;


public class TellerHandler extends ApprovalHandler {

    @Override
    public boolean approve(ApprovalRequest request) {
      if (request.getTransaction().getSenderaccount().getState() != AccountState.ACTIVE){
          System.out.println("❌ Account not active");
          return false;
      }
      if (request.getPinCode() != request.getTransaction().getSenderaccount().getPin_code()){
          System.out.println("❌ Wrong PIN");
          return false;
      }
      double amount=request.getTransaction().getAmount();
      if (request.getTransaction().getSenderaccount().getBalance()<amount){
          System.out.println("❌ Insufficient balance");
          return false;
      }
        return passToNext(request);
    }
    
}
