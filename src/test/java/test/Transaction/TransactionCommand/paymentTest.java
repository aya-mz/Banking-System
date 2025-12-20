package Transaction.TransactionCommand;

import Transaction.Adapter.paymantUi;
import Transaction.Transaction;
import Transaction.TransactionCommand.payment;
import account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class paymentTest {

    @Mock
    paymantUi mockPaymentSystem;

    @Mock
    Transaction mockTransaction;

    @Mock
    Account mockAccount;

    payment paymentCommand;

    @BeforeEach
    void setup() {

        when(mockTransaction.getReciveaccount()).thenReturn(mockAccount);
        when(mockTransaction.getAmount()).thenReturn(100.0);
        when(mockAccount.getBalance()).thenReturn(500.0);

        paymentCommand = new payment(mockTransaction, mockPaymentSystem);
    }

    @Test
    void testExecute() {

        paymentCommand.execute();


        verify(mockAccount, times(1)).getBalance();
        verify(mockAccount, times(1)).setBalance(400.0); // 500 - 100

        verify(mockPaymentSystem, times(1)).Paymoney(100.0);
    }

    @Test
    void testUndo() {

        paymentCommand.execute();
        paymentCommand.undo();

        verify(mockAccount, times(2)).setBalance(anyDouble()); // مرة execute ومرة undo
    }

    @Test
    void testRedo() {
        paymentCommand.redo();
        verify(mockAccount, times(1)).setBalance(400.0);
        verify(mockPaymentSystem, times(1)).Paymoney(100.0);
    }
}
