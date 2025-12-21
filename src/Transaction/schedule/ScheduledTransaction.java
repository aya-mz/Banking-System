package Transaction.schedule;

import java.time.LocalDate;

public interface ScheduledTransaction {
    void execute();
    boolean isDue(LocalDate date);
}

