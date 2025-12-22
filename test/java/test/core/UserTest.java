package core;

import core.user.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserCorrectly() {
        AuthInfo auth = new AuthInfo("password1234");

        UserProfile profile = new UserProfile(
                "Aya",
                "Monzer",
                "Almzayek",
                "Ghada",
                "0940759183",
                "123456789"
        );

        User user = new User("1", "aya@test.com", Role.MANAGER, auth, profile);

        assertEquals("aya@test.com", user.getEmail());
        assertEquals(Role.MANAGER, user.getRole());
        assertNotNull(user.getAuthInfo());
    }

    @Test
    void shouldChangeEmailSuccessfully() {
        AuthInfo auth = new AuthInfo("password1234");
        UserProfile profile = new UserProfile(
                "Aya",
                "Monzer",
                "Almzayek",
                "Ghada",
                "0940759183",
                "123456789"
        );

        User user = new User("1", "aya@test.com", Role.MANAGER, auth, profile);

        UserRepository repo = new UserRepository();
        repo.save(user);

        User updated = repo.changeEmail("aya@test.com", "new@mail.com");

        assertEquals("new@mail.com", updated.getEmail());
    }
}
