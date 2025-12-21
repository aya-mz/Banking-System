package account.states;

import account.Account;
import account.AccountStateInterface;

import java.time.LocalDateTime;

public class SuspendedState implements AccountStateInterface {
    
    private static final SuspendedState instance = new SuspendedState();
    
    /**
     * Singleton Pattern - حالة واحدة فقط لكل التطبيق
     */
    public static SuspendedState getInstance() {
        return instance;
    }
    
    private SuspendedState() {}
    
    @Override
    public boolean canDeposit(Account account) {
        return false; 
    }
    
    @Override
    public boolean canWithdraw(Account account) {
        return false; 
    }
    
    @Override
    public boolean canTransfer(Account account) {
        return false; 
    }
    
    @Override
    public boolean isActive() {
        return false;
    }
    
    @Override
    public boolean isClosed() {
        return false;
    }
    
    @Override
    public String getName() {
        return "SUSPENDED";
    }
    
    @Override
    public void handleDeposit(Account account) {
        throw new IllegalStateException("Cannot deposit to suspended account");
    }
    
    @Override
    public void handleWithdraw(Account account) {
        throw new IllegalStateException("Cannot withdraw from suspended account");
    }
    
    @Override
    public void handleTransfer(Account account) {
        throw new IllegalStateException("Cannot transfer from suspended account");
    }
}

