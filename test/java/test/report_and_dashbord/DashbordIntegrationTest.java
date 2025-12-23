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
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        ticketRepo = new TicketRepository();
        accountRepo = new inmemmory();
        txRepo = new TransactionRepository();

        auditObserver = new AuditLogObserver();
        notificationObserver = new NotificationObserver();

        publisher = new TransactionEventPublisher();
        publisher.subscribe(auditObserver);
        publisher.subscribe(notificationObserver);

        xmlReport = new XMLReort();

        facade = new Facad(txRepo);

        Account acc1 = new Account(1, "Alice", AccountType.SAVING, 1000, 0);
        Account acc2 = new Account(2, "Bob", AccountType.CHECKING, 500, 0);

        Craete createAcc1 = new Craete(acc1, accountRepo);
        Craete createAcc2 = new Craete(acc2, accountRepo);

        createAcc1.execute();
        createAcc2.execute();

        ticketRepo.save(new Ticket(1, Tickettype.TEC, "App crash", new TechnicalIssueStrategy()));
        ticketRepo.save(new Ticket(2, Tickettype.GEN, "Invoice issue", new TechnicalIssueStrategy()));

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

        dashboard = new dashbord(
                xmlReport,
                ticketRepo,
                accountRepo,
                txRepo,
                auditObserver
        );
    }


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
    @Test
    void testSenderAndReceiverExist() {
        accountRepo.save(new Account(1, "Alice", AccountType.SAVING, 1000, 0));
        accountRepo.save(new Account(2, "Bob", AccountType.CHECKING, 500, 0));

        Transaction tx = new Transaction(100, 1,
                accountRepo.findById(1),
                accountRepo.findById(2),
                TransactionType.PAPAL);

        TransactionReportAdapter adapter = new TransactionReportAdapter(tx);
        List<String> report = adapter.toReportData();

        assertTrue(report.get(0).contains("Alice"));
        assertTrue(report.get(0).contains("Bob"));
    }

    @Test
    void testSenderNullReceiverExists() {
        accountRepo.save(new Account(2, "Bob", AccountType.CHECKING, 500, 0));

        Transaction tx = new Transaction(50, 2,
                null,
                accountRepo.findById(2),
                TransactionType.TRANSFER);

        TransactionReportAdapter adapter = new TransactionReportAdapter(tx);
        List<String> report = adapter.toReportData();

        assertTrue(report.get(0).contains("System")); // sender null
        assertTrue(report.get(0).contains("Bob"));
    }

    @Test
    void testSenderExistsReceiverNull() {
        accountRepo.save(new Account(1, "Alice", AccountType.SAVING, 1000, 0));

        Transaction tx = new Transaction(75, 3,
                accountRepo.findById(1),
                null,
                TransactionType.TRANSFER);

        TransactionReportAdapter adapter = new TransactionReportAdapter(tx);
        List<String> report = adapter.toReportData();

        assertTrue(report.get(0).contains("Alice"));
        assertTrue(report.get(0).contains("System")); // receiver null
    }

    @Test
    void testSenderAndReceiverNull() {
        Transaction tx = new Transaction(200, 4,
                null,
                null,
                TransactionType.PAPAL);

        TransactionReportAdapter adapter = new TransactionReportAdapter(tx);
        List<String> report = adapter.toReportData();

        assertTrue(report.get(0).contains("System")); // sender null
        assertTrue(report.get(0).contains("System")); // receiver null
    }
}
