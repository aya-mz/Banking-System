package report_and_dashbord;

import Ticket.*;
import Transaction.*;
import Transaction.Facad.Facad;
import Transaction.observer.*;
import account.*;
import account.Accountcommand.Craete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import report_and_dashbord.ReportGenerateStrategy;
import report_and_dashbord.XMLReort;
import report_and_dashbord.dashbord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DashbordIntegrationTest {

    private dashbord dashboard;
    private TicketRepository ticketRepo;
    private inmemmory accountRepo;
    private TransactionRepository txRepo;

    private AuditLogObserver auditObserver;
    private NotificationObserver notificationObserver;
    private TransactionEventPublisher publisher;

    private ReportGenerateStrategy xmlReport;
    private Facad facade;

    @BeforeEach
    void setUp() {

        // repositories
        ticketRepo = new TicketRepository();
        accountRepo = new inmemmory();
        txRepo = new TransactionRepository();

        // observers
        auditObserver = new AuditLogObserver();
        notificationObserver = new NotificationObserver();

        // publisher
        publisher = new TransactionEventPublisher();
        publisher.subscribe(auditObserver);
        publisher.subscribe(notificationObserver);

        xmlReport = new XMLReort();

        // Facade يستخدم نفس TransactionRepository
        facade = new Facad(txRepo);

        // ===== إنشاء الحسابات =====
        Account acc1 = new Account(1, "Alice", AccountType.SAVING, 1000, 0);
        Account acc2 = new Account(2, "Bob", AccountType.CHECKING, 500, 0);

        Craete createAcc1 = new Craete(acc1, accountRepo);
        Craete createAcc2 = new Craete(acc2, accountRepo);

        createAcc1.execute();
        createAcc2.execute();

        // ===== إنشاء التذاكر =====
        ticketRepo.save(new Ticket(1, Tickettype.TEC, "App crash", new TechnicalIssueStrategy()));
        ticketRepo.save(new Ticket(2, Tickettype.GEN, "Invoice issue", new TechnicalIssueStrategy()));

        // ===== إنشاء المعاملات =====
        Transaction t1 = new Transaction(200, 1, acc1, acc1, TransactionType.PAPAL);
        Transaction t2 = new Transaction(150, 1, acc2, acc1, TransactionType.TRANSFER);

        facade.processTransaction(t1);
        publisher.notifyObservers(
                new TransactionEvent("PAPAL", "Paypal transaction processed")
        );

        facade.processTransaction(t2);
        publisher.notifyObservers(
                new TransactionEvent("TRANSFER", "Transfer transaction processed")
        );

        // ===== Dashboard =====
        dashboard = new dashbord(
                xmlReport,
                ticketRepo,
                accountRepo,
                txRepo,
                auditObserver
        );
    }

    // ===================== TESTS =====================

    @Test
    void testGenerateAllReports() {

        dashboard.generateAccountReport("account_report.xml");
        assertEquals(2, dashboard.totalAccounts());

        dashboard.generateTicketReport("ticket_report.xml");
        assertEquals(2, dashboard.totalTickets());

        dashboard.generateTransactionReport("transaction_report.xml");
        assertEquals(350, dashboard.totalTransactionAmount(), 0.01);

        dashboard.generateAuditReport("audit_report.xml");
        assertEquals(2, auditObserver.getLogs().size());
    }

    @Test
    void testUndoAccountCreation() {

        Account acc3 = new Account(3, "Charlie", AccountType.SAVING, 300, 1);
        Craete createAcc3 = new Craete(acc3, accountRepo);

        createAcc3.execute();
        assertEquals(3, dashboard.totalAccounts());

        createAcc3.undo();
        assertEquals(2, dashboard.totalAccounts());
    }

    @Test
    void testUndoTransaction() {

        facade.undoLast();
        assertEquals(200, dashboard.totalTransactionAmount(), 0.01);
    }

    @Test
    void testObserversTriggered() {

        List<String> auditLogs = auditObserver.getLogs();
        List<String> notifications = notificationObserver.getNotifications();

        assertEquals(2, auditLogs.size());
        assertEquals(2, notifications.size());
    }
}
