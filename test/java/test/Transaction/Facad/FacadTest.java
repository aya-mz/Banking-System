package Transaction.Facad;

import Transaction.*;
import Transaction.Adapter.*;
import Transaction.Facad.Facad;
import Transaction.TransactionCommand.*;
import account.AccountType;
import approval.*;
import core.Command;
import account.Account;

import core.user.AuthInfo;
import core.user.Role;
import core.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FacadTest {
    Account sender;
    Account receiver;
    User adminUser;
    ApprovalHandler chain;
    User normalUser;
    Facad facade;

    @Mock
    paypalAdapter mockPaypalAdapter;

    @Mock
    LegacyAdapter mockLegacyAdapter;

    @Mock
    Transaction mockTransaction;

    @Mock
    Account mockAccount;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        facade = new Facad();


        when(mockTransaction.getReciveaccount()).thenReturn(mockAccount);
        when(mockTransaction.getAmount()).thenReturn(100.0);
        when(mockAccount.getBalance()).thenReturn(500.0);

            MockitoAnnotations.openMocks(this);
            facade = new Facad();

            // Mock الحساب والمعاملة
            sender = new Account(1, "Sender", AccountType.CHECKING, 15000, 1234);
            receiver = new Account(2, "Receiver", AccountType.SAVING, 1000, 0);

            adminUser = new User("1", "admin@test.com", Role.ADMIN, new AuthInfo("admin123"), null);

            // إنشاء Approval Chain
            ApprovalHandler teller = new TellerHandler();
            ApprovalHandler manager = new ManagerHandler();
            ApprovalHandler admin = new AdminHandler();

            teller.setNext(manager);
            manager.setNext(admin);

            chain = teller;

            // Mock Transaction
            when(mockTransaction.getReciveaccount()).thenReturn(mockAccount);
            when(mockTransaction.getAmount()).thenReturn(100.0);
            when(mockAccount.getBalance()).thenReturn(500.0);


    }

    @Test
    void testProcessTransactionSuccess_Paypal() {

        when(mockTransaction.getType()).thenReturn(TransactionType.PAPAL);

        facade.processTransaction(mockTransaction);


        verify(mockAccount, times(1)).getBalance();
        verify(mockAccount, times(1)).setBalance(400.0); // 500 - 100
    }

    @Test
    void testProcessTransactionSuccess_Legacy() {
        when(mockTransaction.getType()).thenReturn(TransactionType.LEGACY_BANK);

        facade.processTransaction(mockTransaction);

        verify(mockAccount, times(1)).getBalance();
        verify(mockAccount, times(1)).setBalance(400.0);
    }

    @Test
    void testProcessTransactionFailure_UnregisteredType() {

        when(mockTransaction.getType()).thenReturn(null);

        facade.processTransaction(mockTransaction);


        assertTrue(facade.commands.isEmpty());
    }

    @Test
    void testUndoLast() {
        when(mockTransaction.getType()).thenReturn(TransactionType.PAPAL);

        facade.processTransaction(mockTransaction);
        facade.undoLast();


        verify(mockAccount, times(2)).setBalance(anyDouble()); // مرة execute و مرة undo
    }

    @Test
    void testRegisterNewTransactionType() {

        TransactionType NEW_TYPE = TransactionType.valueOf("VISA");
        Transaction newTransaction = mock(Transaction.class);
        Account newAccount = mock(Account.class);

        when(newTransaction.getType()).thenReturn(NEW_TYPE);
        when(newTransaction.getReciveaccount()).thenReturn(newAccount);
        when(newTransaction.getAmount()).thenReturn(50.0);
        when(newAccount.getBalance()).thenReturn(100.0);


        facade.registerTransactionType(NEW_TYPE, t -> new payment(t, new paypalAdapter(new paypal())));


        facade.processTransaction(newTransaction);

        verify(newAccount, times(1)).setBalance(50.0); // 100 - 50
    }

    @Test
    void approvedTransaction_shouldExecuteFacade() {
        Transaction tx = new Transaction(500, 1, receiver, sender, TransactionType.PAPAL);
        ApprovalRequest request = new ApprovalRequest(tx, adminUser, sender.getPin_code());

        facade.processTransactionWithApproval(tx, request, chain);

        assertFalse(facade.commands.isEmpty());
    }

    @Test
    void rejectedTransaction_shouldNotExecuteFacade() {
        Transaction tx = new Transaction(500, 1, receiver, sender, TransactionType.PAPAL);

        ApprovalRequest request = new ApprovalRequest(tx, normalUser, 9999);

        facade.processTransactionWithApproval(tx, request, chain);

        assertTrue(facade.commands.isEmpty());
    }


}
