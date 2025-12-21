package account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountComponent - الحساب الفردي (Leaf في Composite Pattern)
 * 
 * هذا هو الحساب الفردي الذي لا يحتوي على حسابات فرعية
 * يمثل Leaf في Composite Pattern
 * 
 * - يطبق AccountComponentInterface (Composite Pattern)
 * - يرث من Account class (للتخزين)
 */
public class AccountComponent extends Account implements AccountComponentInterface {
    public AccountComponent(int user_id, String name, AccountType type, double balance) {
        super(user_id, name, type, balance, 0); // parent_id = 0 
    }
    
    public void add(Account account) {
        throw new UnsupportedOperationException("Individual account cannot have children");
    }
    

    public void remove(Account account) {
        throw new UnsupportedOperationException("Individual account cannot have children");
    }
    
 
    public List<Account> getChildren() {
        return new ArrayList<>(); // قائمة فارغة للحساب الفردي
    }
    

    public boolean isGroup() {
        return false;
    }
    

    public double getTotalBalance() {
        return getBalance();
    }
    
    @Override
    public String toString() {
        return "AccountComponent{" +
                "account_id=" + getAccount_id() +
                ", name='" + getName() + '\'' +
                ", user_id=" + getUser_id() +
                ", type=" + getType() +
                ", balance=" + getBalance() +
                ", state=" + getState() +
                ", Pin_code=" + getPin_code() +
                ", createdAt=" + getCreatedAt() +
                ", updateAt=" + getUpdateAt() +
                '}';
    }
}
