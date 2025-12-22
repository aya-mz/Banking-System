package infrastructure.audit;

public interface AuditWriter {
    void write(String record);
}
