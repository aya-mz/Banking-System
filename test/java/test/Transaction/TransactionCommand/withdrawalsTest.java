package Transaction.TransactionCommand;

import Transaction.Transaction;
import Transaction.TransactionType;
import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.Account;
import account.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class withdrawalsTest {

    private Account sender;

    private Transaction transaction;
    private withdrawals withdrawCommand;

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


        transaction = new Transaction(
                200.0,               // amount
                1,                   // user_id
                null,            // reciveaccount
                sender,              // senderaccount
                TransactionType.withdrawals
        );


        withdrawCommand = new withdrawals(transaction);
    }

    @Test
    void execute_shouldDecreaseSenderBalance() {
        dispatcher.dispatch(
                () -> withdrawCommand.execute(),
                "WITHDRAW_EXECUTED",
                "Money withdrawn from account"
        );

        assertEquals(800.0, sender.getBalance(), 0.001);
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void undo_shouldRestoreSenderBalance() {
        dispatcher.dispatch(
                () -> {
                    withdrawCommand.execute();
                    withdrawCommand.undo();
                },
                "WITHDRAW_UNDO",
                "Withdraw operation undone"
        );

        assertEquals(1000.0, sender.getBalance(), 0.001);
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void redo_shouldWithdrawAgain() {
        dispatcher.dispatch(
                () -> {
                    withdrawCommand.execute();
                    withdrawCommand.undo();
                    withdrawCommand.redo();
                },
                "WITHDRAW_REDO",
                "Withdraw operation redone"
        );

        assertEquals(800.0, sender.getBalance(), 0.001);
        assertEquals(1, audit.getLogs().size());
    }
}
