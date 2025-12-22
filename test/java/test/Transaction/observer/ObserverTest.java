package Transaction.observer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {

    @Test
    void observersReceiveTransactionEvent() {

        TransactionEventPublisher publisher =
                new TransactionEventPublisher();

        AuditLogObserver audit =
                new AuditLogObserver();

        NotificationObserver notification =
                new NotificationObserver();

        publisher.subscribe(audit);
        publisher.subscribe(notification);

        TransactionEvent event =
                new TransactionEvent(
                        "ACCOUNT_CREATED",
                        "Account ID = 1"
                );

        publisher.notifyObservers(event);

        assertEquals(1, audit.getLogs().size());
    }
}
