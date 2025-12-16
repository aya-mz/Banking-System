package core.user;

public class AuthInfo {

    private String passwordHash;
    private String token;

    public AuthInfo(String password) {
        this.passwordHash = hashPassword(password);
        generateToken();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void changePassword(String newPassword) {
        this.passwordHash = hashPassword(newPassword);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    public void generateToken() {
        this.token = "TOKEN_" + System.currentTimeMillis();
    }
}
