package account.states;

import account.Account;
import account.AccountStateInterface;

import java.time.LocalDateTime;


public class FrozenState implements AccountStateInterface {
    
    private static final FrozenState instance = new FrozenState();
    
    /**
     * Singleton Pattern - حالة واحدة فقط لكل التطبيق
     */
    public static FrozenState getInstance() {
        return instance;
    }
    
    private FrozenState() {}
    
    @Override
    public boolean canDeposit(Account account) {
        return true; 
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
        return "FROZEN";
    }
    
    @Override
    public void handleDeposit(Account account) {
        // عند الإيداع، يمكن تفعيل الحساب تلقائياً إذا أصبح الرصيد > 0
        account.setUpdateAt(LocalDateTime.now());
    }
    
    @Override
    public void handleWithdraw(Account account) {
        throw new IllegalStateException("Cannot withdraw from frozen account");
    }
    
    @Override
    public void handleTransfer(Account account) {
        throw new IllegalStateException("Cannot transfer from frozen account");
    }
}

