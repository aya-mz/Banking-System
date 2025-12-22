package Transaction.observer;

import infrastructure.notification.ConsoleNotificationSender;
import infrastructure.notification.NotificationSender;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationObserver implements TransactionObserver {
    private List<String> notifications = new ArrayList<>();

    private final NotificationSender sender = new ConsoleNotificationSender();

    @Override
    public void onTransaction(TransactionEvent event) {
        notifications.add(
                "NOTIFICATION: " + event.getDescription()
        );
        sender.send(event.getType()+" | "+event.getDescription());
        sender.write(event.getType()+" | "+event.getDescription());
    }

    public List<String> getNotifications() {
        return notifications;
    }
}


