package Transaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class TransactionRepository
{
    private List<Transaction> transactions =
            new ArrayList<>();
    private int idCounter = 1;
    public void save(Transaction transaction)
    {
        if (transaction.getTransaction_id() == 0)
        {
            transaction.setTransaction_id(idCounter++);
        }
        transactions.add(transaction);
        System.out.println("Transaction Saved with ID: "
                + transaction.getTransaction_id());
    }
    public List<Transaction>
        findAll()
    {
        return transactions;
    }
    public void delete(
            Transaction transaction)
    { transactions.remove(transaction)
    ; }
    public int getTotalTransactionsCount()
    { return transactions.size();
    }
    public int getTodayTransactionsCount()
    {
        LocalDate today = LocalDate.now();
        return (int) transactions.stream()
                .filter(t -> t.getCreatedAt()
                        != null && t.getCreatedAt()
                        .toLocalDate().equals(today))
                .count();
    }
    public double getTodayTotalAmount()
    { LocalDate today = LocalDate.now();
        return transactions.stream()
                .filter(t -> t.getCreatedAt()
                        != null && t.getCreatedAt().
                        toLocalDate().equals(today))
                .mapToDouble(Transaction::getAmount)
                .sum(); }
}