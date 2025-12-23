package account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountGroup - مجموعة الحسابات (Composite في Composite Pattern)
 * 
 * يمكن أن تحتوي على:
 * - حسابات فردية (AccountComponent)
 * - مجموعات أخرى (AccountGroup)
 * - حسابات عادية (Account)
 * 
 * الرصيد الإجمالي = مجموع رصيد جميع الأبناء
 * 
 * - يطبق AccountComponentInterface (Composite Pattern)
 * - يرث من Account class (للتخزين)
 */
public class AccountGroup extends Account implements AccountComponentInterface {
    
    private List<Account> children;
    

    public AccountGroup(int user_id, String name, AccountType type) {
        super(user_id, name, type, 0.0, 0); // balance = 0, parent_id = 0
        this.children = new ArrayList<>();
    }
    
 
    public void add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Cannot add null account");
        }
        if (account == this) {
            throw new IllegalArgumentException("Cannot add group to itself");
        }
        if (!children.contains(account)) {
            children.add(account);
            setUpdateAt(LocalDateTime.now());
        }
    }
    
 
    public void remove(Account account) {
        if (children.remove(account)) {
            setUpdateAt(LocalDateTime.now());
        }
    }
    

    public List<Account> getChildren() {
        return new ArrayList<>(children); // إرجاع نسخة للسلامة
    }
    

    public boolean isGroup() {
        return true;
    }
    
    /*
      حساب إجمالي الرصيد = مجموع رصيد جميع الأبناء (بشكل متكرر)
     */
    public double getTotalBalance() {
        double total = getBalance(); 
        for (Account child : children) {
            if (child instanceof AccountGroup) {
                AccountGroup group = (AccountGroup) child;
                total += group.getTotalBalance();
            } else {
                total += child.getBalance();
            }
        }
        
        return total;
    }
    
  
    public Account findChildById(int accountId) {
        for (Account child : children) {
            if (child.getAccount_id() == accountId) {
                return child;
            }
            if (child instanceof AccountGroup) {
                AccountGroup group = (AccountGroup) child;
                Account found = group.findChildById(accountId);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
   
    public int getTotalAccountsCount() {
        int count = 1; // حساب المجموعة نفسها
        
        for (Account child : children) {
            if (child instanceof AccountGroup) {
                AccountGroup group = (AccountGroup) child;
                count += group.getTotalAccountsCount();
            } else {
                count++; // حساب فردي أو عادي
            }
        }
        
        return count;
    }
    
    @Override
    public String toString() {
        return "AccountGroup{" +
                "account_id=" + getAccount_id() +
                ", name='" + getName() + '\'' +
                ", user_id=" + getUser_id() +
                ", type=" + getType() +
                ", localBalance=" + getBalance() +
                ", totalBalance=" + getTotalBalance() +
                ", state=" + getState() +
                ", childrenCount=" + children.size() +
                ", totalAccounts=" + getTotalAccountsCount() +
                ", Pin_code=" + getPin_code() +
                ", createdAt=" + getCreatedAt() +
                ", updateAt=" + getUpdateAt() +
                '}';
    }

    @Override
    public void setState(AccountStateInterface state) {
        super.setState(state);

        for (Account child : children) {
            child.setState(state);
        }
    }
}
