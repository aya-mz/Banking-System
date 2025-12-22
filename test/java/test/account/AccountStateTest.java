package account;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.TransactionEventPublisher;
import account.states.ActiveState;
import account.states.ClosedState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountStateTest {

    @Test
    void depositAllowedWhenAccountIsActive() {

        TransactionEventPublisher publisher = new TransactionEventPublisher();
        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

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
