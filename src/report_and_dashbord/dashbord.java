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


    public void generateAccountReport(String fileName) {
        AccountDashboard accDash = new AccountDashboard(accountRepository, reportGenerateStrategy);
        accDash.exportReport(fileName);
    }

    public void generateTicketReport(String fileName) {
        TicketDashboard ticketDash = new TicketDashboard(ticketRepository, reportGenerateStrategy);
       ticketDash.exportTicketReport(fileName);
    }

    public void generateTransactionReport(String fileName) {
        TransactionDashboard txDash = new TransactionDashboard(transactionRepository, reportGenerateStrategy);
      txDash.generateFinalReport(fileName);
    }

    public void generateAuditReport(String fileName) {
        AudiLogDashboard auditDash = new AudiLogDashboard(auditObserver, reportGenerateStrategy);
        auditDash.generateAuditReport(fileName);
    }

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