package core.user;

import core.user.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private Map<String, User> usersByEmail = new HashMap<>();

    public void save(User user) {
        if (usersByEmail.containsKey(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        usersByEmail.put(user.getEmail(), user);
    }

    public User findByEmail(String email) {
        return usersByEmail.get(email);
    }

    public User changeEmail(String oldEmail, String newEmail) {

        if (!usersByEmail.containsKey(oldEmail)) {
            throw new IllegalArgumentException("User not found");
        }

        if (usersByEmail.containsKey(newEmail)) {
            throw new IllegalArgumentException("New email already exists");
        }

        User oldUser = usersByEmail.remove(oldEmail);

        User updatedUser = new User(
                oldUser.getId(),
                newEmail,
                oldUser.getRole(),
                oldUser.getAuthInfo(),
                oldUser.getUserProfile()
        );

        usersByEmail.put(newEmail, updatedUser);
        return updatedUser;
    }
}
