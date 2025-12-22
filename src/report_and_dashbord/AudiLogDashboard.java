package report_and_dashbord;

import Transaction.observer.AuditLogObserver;

import java.util.List;

public class AudiLogDashboard {

    private AuditLogObserver auditObserver;
    private ReportGenerateStrategy reportStrategy;

    public AudiLogDashboard(AuditLogObserver auditObserver, ReportGenerateStrategy reportStrategy) {
        this.auditObserver = auditObserver;
        this.reportStrategy = reportStrategy;
    }

    public void generateAuditReport(String fileName) {
        List<String> logs = auditObserver.getLogs();
        reportStrategy.generate(fileName, logs);
    }
}
