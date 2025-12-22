package account;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.Account;
import account.AccountType;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.decorator.OverdraftProtectionFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverdraftFeatureTest {
    @Test
    void overdraftAllowsNegativeBalanceWithinLimit() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();

        NotificationObserver notification = new NotificationObserver();

        publisher.subscribe(audit);
        publisher.subscribe(notification);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);
        Account acc = new Account(1,"Test", AccountType.CHECKING,100,0);
        AccountFeature feature = new AccountFeatureAdapter(acc);

        feature = new OverdraftProtectionFeature(feature);

        AccountFeature finalFeature = feature;

        dispatcher.dispatch(
                () -> finalFeature.withdraw(500),
                "WITHDRAW",
                "Withdraw with overdraft"
        );

        assertEquals(-400, acc.getBalance(), 0.01);
        assertEquals(1, audit.getLogs().size());
    }

}
