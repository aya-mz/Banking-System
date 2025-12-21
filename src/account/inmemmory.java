package account;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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



    public int allAccountCount() {
        return accounts.size();
    }


    public int todayAccountCount() {
        LocalDate today = LocalDate.now();
        int count = 0;
        for (Account account : accounts.values())
        { if (account.getCreatedAt() != null
                && account.getCreatedAt().toLocalDate()
                .equals(today)) {
            count++;
        }
        }
        return count;
    }

    public int todayAccountCountupdate() {
        LocalDate today = LocalDate.now();
        int count = 0;
        for (Account account : accounts.values())
        {
            if (account.getUpdateAt() != null
                    && account.getUpdateAt().toLocalDate().
                    equals(today))
            {
                count++;
            }
        } return count;
    }

    public List<String> getAccountsSummary() {
        return accounts.values().stream()
                .map(a -> "ID: " + a.getAccount_id() +
                        ", Owner: " + a.getName() +
                        ", Balance: " + a.getBalance() +
                        ", Created: " + a.getCreatedAt() +
                        ", LastUpdated: " + a.getUpdateAt())
                .collect(Collectors.toList());
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
