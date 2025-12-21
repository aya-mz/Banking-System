package Transaction.TransactionCommand;

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
    @BeforeEach
    void setUp() {

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
        transfercommand.execute();
        assertEquals(796.0, sender.getBalance(), 0.001);
        assertEquals(1400.0, reciver.getBalance(), 0.001);


    }

    @Test
    void undo() {
        transfercommand.execute();
        transfercommand.undo();
        assertEquals(1000.0, sender.getBalance(), 0.001);
        assertEquals(1200.0, reciver.getBalance(), 0.001);
    }

    @Test
    void redo() {
        transfercommand.execute();
        transfercommand.undo();
        transfercommand.redo();
        assertEquals(796.0, sender.getBalance(), 0.001);
        assertEquals(1400.0, reciver.getBalance(), 0.001);
    }
}