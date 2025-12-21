package Transaction.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionScheduler {

    private final List<ScheduledTransaction> scheduledTransactions = new ArrayList<>();

    public void add(ScheduledTransaction transaction) {
        scheduledTransactions.add(transaction);
    }

    public void run(LocalDate date) {
        for (ScheduledTransaction tx : scheduledTransactions) {
            if (tx.isDue(date)) {
                tx.execute();
            }
        }
    }
}


