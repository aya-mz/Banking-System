package Transaction.observer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationObserver implements TransactionObserver {
    private List<String> notifications = new ArrayList<>();

    static {
        try (FileWriter fw = new FileWriter("notification.log", false)) {
            fw.write(""); // clear once per JVM run
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransaction(TransactionEvent event) {
        notifications.add(
                "NOTIFICATION: " + event.getDescription()
        );
        System.out.println(
                " Notification | "
                        + event.getType()
                        + " | "
                        + event.getDescription()
        );
        try (FileWriter fw = new FileWriter("notification.log", true)) {
            fw.write(
                    event.getType() + " | " +
                            event.getDescription() + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getNotifications() {
        return notifications;
    }
}


