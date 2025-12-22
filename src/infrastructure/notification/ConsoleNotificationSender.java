package infrastructure.notification;

import java.io.FileWriter;
import java.io.IOException;

public class ConsoleNotificationSender implements NotificationSender {
    static {
        try (FileWriter fw = new FileWriter("notification.log", false)) {
            fw.write(""); // clear once per JVM run
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String message) {
        System.out.println("NOTIFICATION: " + message);
    }

    @Override
    public void write(String record) {
        try (FileWriter fw = new FileWriter("notification.log", true)) {
            fw.write(record + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
