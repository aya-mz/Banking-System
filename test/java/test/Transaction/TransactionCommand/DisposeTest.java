package Transaction.TransactionCommand;

import Transaction.*;
import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.Account;
import account.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisposeTest {
    private Account reciver;

    private Transaction transaction;
    private Dispose disposeCommand;

    TransactionEventPublisher publisher = new TransactionEventPublisher();
    AuditLogObserver audit = new AuditLogObserver();
    NotificationObserver notification = new NotificationObserver();
    TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

    @BeforeEach
    void setUp() {
        publisher.subscribe(audit);
        publisher.subscribe(notification);

        reciver = new Account(
                1,
                "Reciver",
                AccountType.CHECKING,
                1000.0,
                0
        );


        transaction = new Transaction(
                200.0,               // amount
                1,                   // user_id
                reciver,            // reciveaccount
                null,              // senderaccount
                TransactionType.withdrawals
        );


        disposeCommand = new Dispose(transaction);
    }

    @Test
    void execute() {
        dispatcher.dispatch(
                () -> disposeCommand.execute(),
                "DISPOSE",
                "Dispose transaction executed"
        );
        assertEquals(1200.0, reciver.getBalance(), 0.001);

    }

    @Test
    void undo() {
        dispatcher.dispatch(
                () -> {
                    disposeCommand.execute();
                    disposeCommand.undo();
                },
                "DISPOSE_UNDO",
                "Dispose operation undone"
        );
        assertEquals(1000.0, reciver.getBalance(), 0.001);
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void redo() {
        dispatcher.dispatch(
                () -> {
                    disposeCommand.execute();
                    disposeCommand.undo();
                    disposeCommand.redo();
                },
                "DISPOSE_REDO",
                "Dispose operation redone"
        );
        assertEquals(1200.0, reciver.getBalance(), 0.001);
        assertEquals(1, audit.getLogs().size());
    }
}