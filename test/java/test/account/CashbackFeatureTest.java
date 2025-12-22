package account;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.features.AccountFeature;
import account.features.AccountFeatureAdapter;
import account.features.decorator.CashbackFeature;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class CashbackFeatureTest {

    @Test
    void cashbackIsAddedOnDeposit() {

        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();

        NotificationObserver notification = new NotificationObserver();

        publisher.subscribe(audit);
        publisher.subscribe(notification);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        Account acc = new Account(1, "CashbackAcc", AccountType.SAVING, 1000, 0);

        AccountFeature baseFeature = new AccountFeatureAdapter(acc);

        AccountFeature cashbackFeature = new CashbackFeature(baseFeature);

        dispatcher.dispatch(
                () -> cashbackFeature.deposit(1000),
                "DEPOSIT",
                "Deposit with cashback feature"
        );

        assertEquals(2010, acc.getBalance(), 0.01);

        assertEquals(1, audit.getLogs().size());
    }
}
