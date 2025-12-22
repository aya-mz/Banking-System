package Transaction.observer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuditLogObserver implements TransactionObserver {

    private final List<String> logs = new ArrayList<>();

    static {
        try (FileWriter fw = new FileWriter("audit.log", false)) {
            fw.write(""); // clear once per JVM run
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransaction(TransactionEvent event) {
        logs.add(
                event.getTimestamp()
                        + " | "
                        + event.getType()
                        + " | "
                        + event.getDescription()
        );
        try (FileWriter fw = new FileWriter("audit.log", true)) {
            fw.write(
                    event.getType() + " | " +
                            event.getDescription() + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLogs() {
        return logs;
    }
}

