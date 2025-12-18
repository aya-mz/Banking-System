package Transaction.test;

import Transaction.Transaction;
import Transaction.TransactionCommand.withdrawals;
import Transaction.TransactionType;
import account.Account;
import account.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class withdrawalsTest {

    private Account sender;

    private Transaction transaction;
    private withdrawals withdrawCommand;

    @BeforeEach
    void setUp() {

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
        withdrawCommand.execute();
        assertEquals(800.0, sender.getBalance(), 0.001);
    }

    @Test
    void undo_shouldRestoreSenderBalance() {
        withdrawCommand.execute();
        withdrawCommand.undo();
        assertEquals(1000.0, sender.getBalance(), 0.001);
    }

    @Test
    void redo_shouldWithdrawAgain() {
        withdrawCommand.execute();
        withdrawCommand.undo();
        withdrawCommand.redo();
        assertEquals(800.0, sender.getBalance(), 0.001);
    }
}
