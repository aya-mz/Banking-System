package account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class inmemmory {

    private Map<Integer, Account> accounts = new HashMap<>();
    private int idCounter = 1;

    public void save(Account account) {
        if (account.getAccount_id() == 0) {
            account.setAccount_id(idCounter++);
        }
        accounts.put(account.getAccount_id(), account);
    }

    public Account findById(int accountId) {
        return accounts.get(accountId);
    }

    public void delete(int accountId) {
        accounts.remove(accountId);
    }

    public Map<Integer, Account> findAll() {
        return accounts;
    }


    public List<Account> findAllChildren(int parentId) {
        List<Account> result = new ArrayList<>();
        findChildrenRecursive(parentId, result);
        return result;
    }

    private void findChildrenRecursive(int parentId, List<Account> result) {
        for (Account account : accounts.values()) {
            if (account.getParent_id() == parentId) {
                result.add(account);
                findChildrenRecursive(account.getAccount_id(), result);
            }
        }
    }
}
