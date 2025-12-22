package infrastructure.audit;

import java.io.FileWriter;
import java.io.IOException;

public class FileAuditWriter implements AuditWriter {
    static {
        try (FileWriter fw = new FileWriter("audit.log", false)) {
            fw.write(""); // clear once per JVM run
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void write(String record) {
        try (FileWriter fw = new FileWriter("audit.log", true)) {
            fw.write(record + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
