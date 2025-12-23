package infrastructure.bootstrap;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.*;

public class SystemBootstrap {

    public TransactionDispatcher auditOnly() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();
        publisher.subscribe(new AuditLogObserver());
        return new TransactionDispatcher(publisher);
    }

    public TransactionDispatcher auditAndNotification() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();
        publisher.subscribe(new AuditLogObserver());
        publisher.subscribe(new NotificationObserver());
        return new TransactionDispatcher(publisher);
    }

    public TransactionDispatcher notificationOnly() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();
        publisher.subscribe(new NotificationObserver());
        return new TransactionDispatcher(publisher);
    }
}
