package Transaction.observer;

import infrastructure.audit.AuditWriter;
import infrastructure.audit.FileAuditWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuditLogObserver implements TransactionObserver {

    private final List<String> logs = new ArrayList<>();
    private final AuditWriter writer = new FileAuditWriter();

    @Override
    public void onTransaction(TransactionEvent event) {
        logs.add(
                event.getTimestamp()
                        + " | "
                        + event.getType()
                        + " | "
                        + event.getDescription()
        );
        writer.write(
                event.getTimestamp()
                        + " | " + event.getType()
                        + " | " + event.getDescription()
        );
    }

    public List<String> getLogs() {
        return logs;
    }
}

