
package java.test.account.AccountStateTest;

import account.Account;
import account.AccountType;
import account.states.ClosedState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountClosedStateTest {

    @Test
    void closedAccount_shouldRejectAllOperations() {

        Account account =
                new Account(1, "ClosedAcc", AccountType.SAVING, 0, 0);

        account.setState(ClosedState.getInstance());

        assertTrue(account.getState().isClosed());
        assertFalse(account.getState().canDeposit(account));
        assertFalse(account.getState().canWithdraw(account));
        assertFalse(account.getState().canTransfer(account));
    }
}

