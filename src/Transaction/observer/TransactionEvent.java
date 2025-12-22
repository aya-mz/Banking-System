package Transaction.observer;

import java.time.LocalDateTime;

public class TransactionEvent {

    private final String type;
    private final String description;
    private final LocalDateTime timestamp;

    public TransactionEvent(String type, String description) {
        this.type = type;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}


