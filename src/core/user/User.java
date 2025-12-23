package core.user;

public class User {

    private final int id;
    private final String email;
    private Role role;
    private AuthInfo authInfo;
    private UserProfile userProfile;

    public User(int id, String email, Role role, AuthInfo authInfo, UserProfile userProfile) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.authInfo = authInfo;
        this.userProfile = userProfile;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
