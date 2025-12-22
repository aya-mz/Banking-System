package report_and_dashbord;

import Ticket.TicketRepository;
import Transaction.TransactionRepository;
import account.inmemmory;
import Transaction.observer.AuditLogObserver;

import java.util.List;

public class dashbord {
    private ReportGenerateStrategy reportGenerateStrategy;
    private TicketRepository ticketRepository;
    private inmemmory accountRepository;
    private TransactionRepository transactionRepository;
    private AuditLogObserver auditObserver;

    // الـ Constructor لربط جميع المستودعات
    public dashbord(ReportGenerateStrategy strategy,
                    TicketRepository ticketRepo,
                    inmemmory accountRepo,
                    TransactionRepository txRepo,
                    AuditLogObserver auditObserver) {
        this.reportGenerateStrategy = strategy;
        this.ticketRepository = ticketRepo;
        this.accountRepository = accountRepo;
        this.transactionRepository = txRepo;
        this.auditObserver = auditObserver;
    }

    // 1. وظيفة خاصة لتقرير الحسابات فقط
    public void generateAccountReport(String fileName) {
        AccountDashboard accDash = new AccountDashboard(accountRepository, reportGenerateStrategy);
        List<String> data = accDash.getFullReport();
        reportGenerateStrategy.generate(fileName, data);
    }

    // 2. وظيفة خاصة لتقرير التذاكر فقط
    public void generateTicketReport(String fileName) {
        TicketDashboard ticketDash = new TicketDashboard(ticketRepository, reportGenerateStrategy);
        List<String> data = ticketDash.getTicketsFullSummary();
        reportGenerateStrategy.generate(fileName, data);
    }

    // 3. وظيفة خاصة لتقرير المعاملات المالية فقط
    public void generateTransactionReport(String fileName) {
        TransactionDashboard txDash = new TransactionDashboard(transactionRepository, reportGenerateStrategy);
        List<String> data = txDash.getFullTransactionReportData();
        reportGenerateStrategy.generate(fileName, data);
    }

    // 4. وظيفة خاصة لتقرير سجل الرقابة (Audit Log) فقط
    public void generateAuditReport(String fileName) {
        AudiLogDashboard auditDash = new AudiLogDashboard(auditObserver, reportGenerateStrategy);
        auditDash.generateAuditReport(fileName);
    }

    // --- ميثودات الإحصائيات السريعة (Getters) ---
    public int totalTickets() {
        return ticketRepository.getTotalTickets();
    }

    public int totalAccounts() {
        return accountRepository.allAccountCount();
    }

    public double totalTransactionAmount() {
        return transactionRepository.getTodayTotalAmount();
    }
}