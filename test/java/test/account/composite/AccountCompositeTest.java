package account;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class AccountCompositeTest {

    private AccountComponent account1;
    private AccountComponent account2;
    private AccountGroup mainGroup;
    private AccountGroup subGroup;

    @BeforeEach
    void setup() {
        account1 = new AccountComponent(1, "Account 1", AccountType.SAVING, 100);
        account2 = new AccountComponent(2, "Account 2", AccountType.CHECKING, 200);

        mainGroup = new AccountGroup(10, "Main Group", AccountType.SAVING);
        subGroup = new AccountGroup(11, "Sub Group", AccountType.CHECKING);
    }

    @Test
    void testIndividualAccountIsLeaf() {
        assertFalse(account1.isGroup());
        assertEquals(100, account1.getTotalBalance());
        assertTrue(account1.getChildren().isEmpty());
    }

    @Test
    void testAddAccountsToGroup() {
        mainGroup.add(account1);
        mainGroup.add(account2);

        assertEquals(2, mainGroup.getChildren().size());
    }

    @Test
    void testAddSubGroupToGroup() {
        subGroup.add(account1);
        mainGroup.add(subGroup);

        assertEquals(1, mainGroup.getChildren().size());
        assertEquals(100, mainGroup.getTotalBalance());
    }

    @Test
    void testNestedGroupsTotalBalance() {
        subGroup.add(account1);
        subGroup.add(account2);
        mainGroup.add(subGroup);

        assertEquals(300, mainGroup.getTotalBalance());
    }

    @Test
    void testRemoveAccountFromGroup() {
        mainGroup.add(account1);
        mainGroup.remove(account1);

        assertTrue(mainGroup.getChildren().isEmpty());
    }

    @Test
    void testFindChildById() {
        account1.setAccount_id(1);
        account2.setAccount_id(2);

        subGroup.add(account1);
        mainGroup.add(subGroup);
        mainGroup.add(account2);

        Account found = mainGroup.findChildById(1);
        assertNotNull(found);
        assertEquals("Account 1", found.getName());
    }

    @Test
    void testTotalAccountsCount() {
        subGroup.add(account1);
        subGroup.add(account2);
        mainGroup.add(subGroup);

        assertEquals(4, mainGroup.getTotalAccountsCount());
        // (MainGroup + SubGroup + 2 Accounts)
    }

    @Test
    void testPreventAddNullAccount() {
        assertThrows(IllegalArgumentException.class, () -> mainGroup.add(null));
    }

    @Test
    void testPreventAddGroupToItself() {
        assertThrows(IllegalArgumentException.class, () -> mainGroup.add(mainGroup));
    }

    @Test
    void testLeafAddThrowsException() {
        assertThrows(UnsupportedOperationException.class,
                () -> account1.add(account2));
    }
}
