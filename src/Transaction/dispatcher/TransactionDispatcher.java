package Transaction.dispatcher;

import Transaction.observer.TransactionEvent;
import Transaction.observer.TransactionEventPublisher;

public class TransactionDispatcher {

    private final TransactionEventPublisher publisher;

    public TransactionDispatcher(TransactionEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void dispatch(Runnable transaction, String type, String description) {
        transaction.run();
        publisher.notifyObservers(new TransactionEvent(type, description));
    }
}
