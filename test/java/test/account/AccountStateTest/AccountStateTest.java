package account;
import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import account.states.ActiveState;
import account.states.ClosedState;
import account.states.FrozenState;
import account.states.SuspendedState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountStateTest {
    private Account account;

    @BeforeEach
    void setup() {
        account = new Account(1, "Test Account", AccountType.SAVING, 500, 0);
    }


    @Test
    void testActiveStatePermissions() {
        account.setState(ActiveState.getInstance());

        assertTrue(account.getState().canDeposit(account));
        assertTrue(account.getState().canWithdraw(account));
        assertTrue(account.getState().canTransfer(account));
        assertTrue(account.getState().isActive());
        assertFalse(account.getState().isClosed());
    }

    @Test
    void testFrozenStatePermissions() {
        account.setState(FrozenState.getInstance());

        assertTrue(account.getState().canDeposit(account));
        assertFalse(account.getState().canWithdraw(account));
        assertFalse(account.getState().canTransfer(account));
        assertFalse(account.getState().isActive());
    }

    @Test
    void testSuspendedStatePermissions() {
        account.setState(SuspendedState.getInstance());

        assertFalse(account.getState().canDeposit(account));
        assertFalse(account.getState().canWithdraw(account));
        assertFalse(account.getState().canTransfer(account));
    }

    @Test
    void testClosedStatePermissions() {
        account.setState(ClosedState.getInstance());

        assertFalse(account.getState().canDeposit(account));
        assertFalse(account.getState().canWithdraw(account));
        assertFalse(account.getState().canTransfer(account));
        assertTrue(account.getState().isClosed());
    }

    @Test
    void testActiveStateHandlers() {
        account.setState(ActiveState.getInstance());

        assertDoesNotThrow(() -> account.getState().handleDeposit(account));
        assertDoesNotThrow(() -> account.getState().handleWithdraw(account));
        assertDoesNotThrow(() -> account.getState().handleTransfer(account));
    }

    @Test
    void testFrozenStateWithdrawThrowsException() {
        account.setState(FrozenState.getInstance());

        assertThrows(IllegalStateException.class,
                () -> account.getState().handleWithdraw(account));
    }

    @Test
    void testSuspendedStateDepositThrowsException() {
        account.setState(SuspendedState.getInstance());

        assertThrows(IllegalStateException.class,
                () -> account.getState().handleDeposit(account));
    }

    @Test
    void testClosedStateAllOperationsThrowException() {
        account.setState(ClosedState.getInstance());

        assertThrows(IllegalStateException.class,
                () -> account.getState().handleDeposit(account));

        assertThrows(IllegalStateException.class,
                () -> account.getState().handleWithdraw(account));

        assertThrows(IllegalStateException.class,
                () -> account.getState().handleTransfer(account));
    }

    @Test
    void testStateNames() {
        assertEquals("ACTIVE", ActiveState.getInstance().getName());
        assertEquals("FROZEN", FrozenState.getInstance().getName());
        assertEquals("SUSPENDED", SuspendedState.getInstance().getName());
        assertEquals("CLOSED", ClosedState.getInstance().getName());
    }

    @Test
    void depositAllowedWhenAccountIsActive() {

        TransactionEventPublisher publisher = new TransactionEventPublisher();
        AuditLogObserver audit = new AuditLogObserver();
        NotificationObserver notification = new NotificationObserver();

        publisher.subscribe(audit);
        publisher.subscribe(notification);

        TransactionDispatcher dispatcher =
                new TransactionDispatcher(publisher);

        Account acc =
                new Account(1, "ActiveAcc", AccountType.SAVING, 1000, 0);

        acc.setState(ActiveState.getInstance());

        dispatcher.dispatch(
                () -> acc.getState().handleDeposit(acc),
                "DEPOSIT",
                "Deposit on active account"
        );

        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void depositNotAllowedWhenAccountIsClosed() {

        TransactionEventPublisher publisher = new TransactionEventPublisher();
        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher =
                new TransactionDispatcher(publisher);

        Account acc =
                new Account(1, "ClosedAcc", AccountType.SAVING, 1000, 0);

        acc.setState(ClosedState.getInstance());

        assertThrows(IllegalStateException.class, () -> {
            dispatcher.dispatch(
                    () -> acc.getState().handleDeposit(acc),
                    "DEPOSIT",
                    "Deposit on closed account"
            );
        });

        assertEquals(0, audit.getLogs().size());
    }
}
