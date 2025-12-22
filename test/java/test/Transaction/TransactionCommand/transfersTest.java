package Transaction.TransactionCommand;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.*;
import Transaction.Transaction;
import Transaction.TransactionType;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class transfersTest {

    private Account sender;
private  Account reciver;
    private Transaction transaction;
    private transfers transfercommand;

    TransactionEventPublisher publisher = new TransactionEventPublisher();
    AuditLogObserver audit = new AuditLogObserver();
    NotificationObserver notification = new NotificationObserver();
    TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

    @BeforeEach
    void setUp() {
        publisher.subscribe(audit);
        publisher.subscribe(notification);


        sender = new Account(
                1,
                "Sender",
                AccountType.CHECKING,
                1000.0,
                0
        );

reciver = new Account(2,
                     "reciver",
                      AccountType.CHECKING ,
                      1200.0 ,
                      0);

        transaction = new Transaction(
                200.0,               // amount
                1,                   // user_id
                reciver,            // reciveaccount
                sender,              // senderaccount
                TransactionType.withdrawals
        );


        transfercommand = new transfers(transaction);
    }

    @Test
    void execute() {
        dispatcher.dispatch(
                () -> transfercommand.execute(),
                "TRANSFER_EXECUTED",
                "Money transferred from sender to receiver"
        );

        assertEquals(796.0, sender.getBalance(), 0.001);
        assertEquals(1400.0, reciver.getBalance(), 0.001);
        assertEquals(1, audit.getLogs().size());
    }


    @Test
    void undo() {
        dispatcher.dispatch(
                () -> {
                    transfercommand.execute();
                    transfercommand.undo();
                },
                "TRANSFER_UNDO",
                "Transfer operation undone"
        );

        assertEquals(1000.0, sender.getBalance(), 0.001);
        assertEquals(1200.0, reciver.getBalance(), 0.001);
        assertEquals(1, audit.getLogs().size());
    }


    @Test
    void redo() {
        dispatcher.dispatch(
                () -> {
                    transfercommand.execute();
                    transfercommand.undo();
                    transfercommand.redo();
                },
                "TRANSFER_REDO",
                "Transfer operation redone"
        );

        assertEquals(796.0, sender.getBalance(), 0.001);
        assertEquals(1400.0, reciver.getBalance(), 0.001);
        assertEquals(1, audit.getLogs().size());
    }

}