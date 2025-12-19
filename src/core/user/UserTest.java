package core.user;

import approval.AdminHandler;
import approval.ManagerHandler;
import approval.TellerHandler;

public class UserTest {
    public void test1(){
        AuthInfo auth = new AuthInfo("password1234");

        UserProfile profile = new UserProfile("Aya", "Monzer", "Almzayek", "Ghada","0940759183", "123456789");

        User user = new User("1", "aya@test.com", Role.MANAGER, auth, profile);

        if (user.getEmail().equals("ayaa@test.com")) {
            System.out.println("Email test passed");
        } else {
            System.out.println("Email test failed");
        }

        if (user.getRole() == Role.MANAGER) {
            System.out.println("Role test passed");
        } else {
            System.out.println("Role test failed");
        }

        if (user.getAuthInfo() != null) {
            System.out.println("AuthInfo test passed");
        } else {
            System.out.println("AuthInfo test failed");
        }
        TellerHandler teller=new TellerHandler();
        ManagerHandler manager=new ManagerHandler();
        AdminHandler admin=new AdminHandler();
          teller.setNext(manager);
        manager.setNext(admin);

        System.out.println("=== APPROVAL TEST ===");

        double[] amounts = {500, 2000, 7000}; // مبالغ للاختبار

        for (double amount : amounts) {
            boolean approved = teller.approve(user, "WITHDRAW", amount);
            System.out.println("Amount " + amount + " approved for role " + user.getRole() + "? " + approved);
        }
        UserRepository repo = new UserRepository();

        repo.save(user);

        User updated = repo.changeEmail("aya@test.com", "new@mail.com");

        if(!updated.getEmail().equals("new@mail.com")){
            System.out.println("Email has not changed..");
        }else {
            System.out.println("Email has changed successfully..");
        }

    }


}