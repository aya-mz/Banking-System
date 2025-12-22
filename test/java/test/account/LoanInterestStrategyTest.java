package account;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.strategy.InterestApplier;
import account.features.strategy.LoanInterestStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoanInterestStrategyTest {

    @Test
    void loanInterestIsAppliedAsDebtIncrease() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        Account loanAccount = new Account(1, "LoanAcc", AccountType.LOAN, 1000, 0);

        AccountFeature feature = new AccountFeatureAdapter(loanAccount);

        LoanInterestStrategy strategy = new LoanInterestStrategy();

        InterestApplier scheduler = new InterestApplier(strategy);

        dispatcher.dispatch(
                () -> scheduler.apply(feature),
                "INTEREST_APPLIED",
                "Loan interest applied"
        );

        assertEquals(950, loanAccount.getBalance(), 0.01);
        assertEquals(1, audit.getLogs().size());
    }
}
