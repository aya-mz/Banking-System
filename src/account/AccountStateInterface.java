package account;


public interface AccountStateInterface {
    
    
    boolean canDeposit(Account account);
    

    boolean canWithdraw(Account account);
    

    boolean canTransfer(Account account);
    

    boolean isActive();
    

    boolean isClosed();
    

    String getName();
    

    void handleDeposit(Account account);
    
  
    void handleWithdraw(Account account);
    
   
    void handleTransfer(Account account);
}

