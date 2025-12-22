package account;

import account.Accountcommand.ChangeState;
import account.states.ActiveState;
import account.states.FrozenState;
import account.states.SuspendedState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountStateTransitionTest {

    @Test
    void accountState_shouldChangeFromActiveToFrozen() {

        Account account =
                new Account(1, "TestAcc", AccountType.SAVING, 5000, 0);

        assertTrue(account.getState().isActive());

        ChangeState changeToFrozen =
                new ChangeState(account, FrozenState.getInstance());

        changeToFrozen.execute();

        assertEquals("FROZEN", account.getState().getName());
        assertFalse(account.getState().canWithdraw(account));
    }

    @Test
    void undo_shouldRestorePreviousState() {

        Account account =
                new Account(1, "TestAcc", AccountType.SAVING, 5000, 0);

        ChangeState changeToSuspended =
                new ChangeState(account, SuspendedState.getInstance());

        changeToSuspended.execute();
        assertEquals("SUSPENDED", account.getState().getName());

        changeToSuspended.undo();
        assertEquals("ACTIVE", account.getState().getName());
    }
}
