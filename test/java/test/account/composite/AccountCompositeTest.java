package account;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.TransactionEventPublisher;
import account.states.ClosedState;
import Transaction.observer.AuditLogObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountCompositeTest {

    @Test
    void childAccountBlockedWhenParentIsClosed() {

        TransactionEventPublisher publisher = new TransactionEventPublisher();
        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher =
                new TransactionDispatcher(publisher);

        Account parent =
                new Account(1, "ParentAcc", AccountType.SAVING, 2000, 0);
        parent.setState(ClosedState.getInstance());

        Account child =
                new Account(1, "ChildAcc", AccountType.SAVING, 500, parent.getAccount_id());

        assertTrue(parent.getState().isClosed());

        assertThrows(IllegalStateException.class, () -> {
            dispatcher.dispatch(
                    () -> parent.getState().handleWithdraw(parent),
                    "WITHDRAW",
                    "Withdraw from child while parent closed"
            );
        });

        assertEquals(0, audit.getLogs().size());
    }



    @Test
    void groupReturnsSumOfChildrenBalances() {
        Account acc1 = new Account(1, "A1", AccountType.SAVING, 1000, 0);
        Account acc2 = new Account(1, "A2", AccountType.CHECKING, 2000, 0);

        AccountGroup group = new AccountGroup(1, "Group", AccountType.CHECKING);

        group.add(acc1);
        group.add(acc2);

        assertEquals(3000, group.getTotalBalance());
    }

}
