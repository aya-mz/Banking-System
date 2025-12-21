package Transaction.schedule;

import account.features.AccountFeature;
import account.features.strategy.InterestStrategy;

import java.time.LocalDate;

public class MonthlyInterestTransaction implements ScheduledTransaction {

    private final AccountFeature account;
    private final InterestStrategy interestStrategy;
    private LocalDate lastExecution;

    public MonthlyInterestTransaction(AccountFeature account, InterestStrategy interestStrategy) {
        this.account = account;
        this.interestStrategy = interestStrategy;
        this.lastExecution = LocalDate.now().minusMonths(1);
    }

    @Override
    public boolean isDue(LocalDate date) {
        return !lastExecution.plusMonths(1).isAfter(date);
    }

    @Override
    public void execute() {
        double interest = interestStrategy.calculateInterest(account);
        account.deposit(interest);
        lastExecution = LocalDate.now();
    }
}


