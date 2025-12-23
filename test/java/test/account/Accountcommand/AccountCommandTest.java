package account.Accountcommand;

import account.*;
import account.states.ActiveState;
import account.states.ClosedState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountCommandTest {

    private inmemmory repo;
    private Account account;

    @BeforeEach
    void setUp() {
        repo = new inmemmory();
        account = new Account(0, "Ali", AccountType.SAVING, 500, 1234);
        account.setState(ActiveState.getInstance());
        repo.save(account);
    }

    @Test
    void create_success_regularAccount() {
        Account acc = new Account(1, "Ali", AccountType.SAVING, 150, 0);
        Craete cmd = new Craete(acc, repo);
        cmd.execute();
        assertEquals(acc, repo.findById(acc.getAccount_id()));
    }

    @Test
    void create_fail_noName() {
        Account acc = new Account(2, "", AccountType.SAVING, 200, 1234);
        Craete cmd = new Craete(acc, repo);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void create_fail_nullName() {
        Account acc = new Account(3, null, AccountType.SAVING, 200, 1234);
        Craete cmd = new Craete(acc, repo);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void create_fail_noType() {
        Account acc = new Account(4, "Bob", null, 200, 1234);
        Craete cmd = new Craete(acc, repo);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void create_fail_lowBalance_nonLoan() {
        Account acc = new Account(5, "Charlie", AccountType.SAVING, 50, 1234);
        Craete cmd = new Craete(acc, repo);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void create_success_lowBalance_loan() {
        Account acc = new Account(6, "LoanAcc", AccountType.LOAN, 50, 0);
        Craete cmd = new Craete(acc, repo);
        cmd.execute();
        assertEquals(acc, repo.findById(acc.getAccount_id()));
    }

    @Test
    void create_success_withParent() {
        Account parent = new Account(7, "Parent", AccountType.SAVING, 200, 1234);
        repo.save(parent);
        Account child = new Account(8, "Child", AccountType.SAVING, 150, 1234);
        child.setParent_id(parent.getAccount_id());
        Craete cmd = new Craete(child, repo);
        cmd.execute();
        assertEquals(child, repo.findById(child.getAccount_id()));
    }

    @Test
    void create_fail_parentNotFound() {
        Account child = new Account(9, "Orphan", AccountType.SAVING, 150, 1234);
        child.setParent_id(999);
        Craete cmd = new Craete(child, repo);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void create_fail_parentClosed() {
        Account parent = new Account(10, "ClosedParent", AccountType.SAVING, 200, 1234);
        parent.setState(ClosedState.getInstance());
        repo.save(parent);
        Account child = new Account(11, "Child", AccountType.SAVING, 150, 1234);
        child.setParent_id(parent.getAccount_id());
        Craete cmd = new Craete(child, repo);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void undo_and_redo() {
        Account acc = new Account(12, "UndoRedo", AccountType.SAVING, 150, 0);
        Craete cmd = new Craete(acc, repo);
        cmd.execute();
        cmd.undo();
        assertNull(repo.findById(acc.getAccount_id()));
        cmd.redo();
        assertEquals(acc, repo.findById(acc.getAccount_id()));
    }
    @Test
    void changePin_success() {
        int oldPin = account.getPin_code();
        ChangePinCode cmd = new ChangePinCode(account, 9999);

        cmd.execute();
        assertEquals(9999, account.getPin_code());

        cmd.undo();
        assertEquals(oldPin, account.getPin_code());

        cmd.redo();
        assertEquals(9999, account.getPin_code());
    }

    @Test
    void changeType_success() {
        ChangeType cmd = new ChangeType(account, AccountType.CHECKING);

        cmd.execute();
        assertEquals(AccountType.CHECKING, account.getType());

        cmd.undo();
        assertEquals(AccountType.SAVING, account.getType());

        cmd.redo();
        assertEquals(AccountType.CHECKING, account.getType());
    }

    @Test
    void changeState_success() {
        ChangeState cmd = new ChangeState(account, ActiveState.getInstance());

        cmd.execute();
        assertTrue(account.getState().isActive());

        cmd.undo();
        assertTrue(account.getState().isActive());

        cmd.redo();
        assertTrue(account.getState().isActive());
    }

    @Test
    void changeState_fail_toClosed() {
        ChangeState cmd = new ChangeState(account, ClosedState.getInstance());
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void closeAccount_success() {
        account.setBalance(0);
        Close cmd = new Close(account, repo);

        cmd.execute();
        assertTrue(account.getState().isClosed());
    }

    @Test
    void closeAccount_fail_balanceNotZero() {
        Close cmd = new Close(account, repo);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void closeAccount_fail_activeChildExists() {
        Account child = new Account(0, "Child", AccountType.SAVING, 0, 2222);
        child.setParent_id(account.getAccount_id());
        child.setState(ActiveState.getInstance());
        repo.save(child);

        account.setBalance(0);
        Close cmd = new Close(account, repo);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }

    @Test
    void closeAccount_success_withInactiveChild() {
        Account child = new Account(0, "Child", AccountType.SAVING, 0, 2222);
        child.setParent_id(account.getAccount_id());
        child.setState(ClosedState.getInstance());
        repo.save(child);

        account.setBalance(0);
        Close cmd = new Close(account, repo);
        cmd.execute();

        assertTrue(account.getState().isClosed());
    }
    @Test
    void closeAccount_undoRedoException() {
        account.setBalance(0);
        Close cmd = new Close(account, repo);
        cmd.execute();
        assertThrows(IllegalArgumentException.class, cmd::undo);
        assertThrows(IllegalArgumentException.class, cmd::redo);
    }
    @Test
    void closeAccount_fail_loan_withBalance() {
        Account loanAcc = new Account(0, "Loan", AccountType.LOAN, 500, 3333);
        repo.save(loanAcc);
        Close cmd = new Close(loanAcc, repo);
        cmd.execute();
        assertTrue(loanAcc.getState().isClosed());
    }



}
