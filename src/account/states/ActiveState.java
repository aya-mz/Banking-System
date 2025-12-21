package account.states;

import account.Account;
import account.AccountStateInterface;

import java.time.LocalDateTime;


public class ActiveState implements AccountStateInterface {
    
    private static final ActiveState instance = new ActiveState();
    
    /**
     * Singleton Pattern - حالة واحدة فقط لكل التطبيق
     */
    public static ActiveState getInstance() {
        return instance;
    }
    
    private ActiveState() {}
    
    @Override
    public boolean canDeposit(Account account) {
        return true; 
    }
    
    @Override
    public boolean canWithdraw(Account account) {
        return true; 
    }
    
    @Override
    public boolean canTransfer(Account account) {
        return true; 
    }
    
    @Override
    public boolean isActive() {
        return true;
    }
    
    @Override
    public boolean isClosed() {
        return false;
    }
    
    @Override
    public String getName() {
        return "ACTIVE";
    }
    
    @Override
    public void handleDeposit(Account account) {
        account.setUpdateAt(LocalDateTime.now());
    }
    
    @Override
    public void handleWithdraw(Account account) {
        account.setUpdateAt(LocalDateTime.now());
    }
    
    @Override
    public void handleTransfer(Account account) {
        account.setUpdateAt(LocalDateTime.now());
    }
}

