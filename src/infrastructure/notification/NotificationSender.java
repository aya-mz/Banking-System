package infrastructure.notification;

public interface NotificationSender {
    void send(String message);
    void write(String record);
}
