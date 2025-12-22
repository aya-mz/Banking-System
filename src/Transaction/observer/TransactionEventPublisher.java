package Transaction.observer;

import java.util.ArrayList;
import java.util.List;

public class TransactionEventPublisher {

    private final List<TransactionObserver> observers = new ArrayList<>();

    public void subscribe(TransactionObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(TransactionEvent event) {
        for (TransactionObserver observer : observers) {
            observer.onTransaction(event);
        }
    }
}

