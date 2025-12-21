package account;

import java.util.List;

public interface AccountComponentInterface {
    
    
    void add(Account account);
    
   
    void remove(Account account);
    
    
    List<Account> getChildren();
    
    
    boolean isGroup();
    
    
    double getTotalBalance();
}

