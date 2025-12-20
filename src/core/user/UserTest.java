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