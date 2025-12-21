package Transaction.TransactionCommand;

import Transaction.*;
import account.Account;
import account.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisposeTest {
    private Account reciver;

    private Transaction transaction;
    private Dispose disposeCommand;
    @BeforeEach
    void setUp() {

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
        disposeCommand.execute();
        assertEquals(1200.0, reciver.getBalance(), 0.001);

    }

    @Test
    void undo() {
        disposeCommand.execute();
        disposeCommand.undo();
        assertEquals(1000.0, reciver.getBalance(), 0.001);
    }

    @Test
    void redo() {
        disposeCommand.execute();
        disposeCommand.undo();
        disposeCommand.redo();
        assertEquals(1200.0, reciver.getBalance(), 0.001);
    }
}