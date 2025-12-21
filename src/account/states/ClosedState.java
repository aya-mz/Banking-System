package account.states;

import account.Account;
import account.AccountStateInterface;

import java.time.LocalDateTime;


public class ClosedState implements AccountStateInterface {
    
    private static final ClosedState instance = new ClosedState();
    
    /**
     * Singleton Pattern - حالة واحدة فقط لكل التطبيق
     */
    public static ClosedState getInstance() {
        return instance;
    }
    
    private ClosedState() {}
    
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
        return true; 
    }
    
    @Override
    public String getName() {
        return "CLOSED";
    }
    
    @Override
    public void handleDeposit(Account account) {
        throw new IllegalStateException("Cannot deposit to closed account");
    }
    
    @Override
    public void handleWithdraw(Account account) {
        throw new IllegalStateException("Cannot withdraw from closed account");
    }
    
    @Override
    public void handleTransfer(Account account) {
        throw new IllegalStateException("Cannot transfer from closed account");
    }
}

