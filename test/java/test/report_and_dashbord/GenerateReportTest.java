package report_and_dashbord;

import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerateReportTest {

    @Test
    void testGeneratePDFReport() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        ReportGenerateStrategy pdfReport = new PDFReport();
        List<String> data = List.of(
                "ID: 1, Type: TRANSFER, Amount: 500, Sender: 1001, Receiver: 1002, Date: today"
        );
        String fileName = "test_report.pdf";


        dispatcher.dispatch(
                () -> pdfReport.generate(fileName, data),
                "REPORT_GENERATED",
                "PDF report generated"
        );

        File file = new File(fileName);
        assertTrue(file.exists(), "PDF file should be generated");
        assertTrue(file.length() > 0, "PDF file should not be empty");

        assertEquals(1, audit.getLogs().size());

        file.delete();
    }

    @Test
    void testGenerateXMLReport() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        ReportGenerateStrategy xmlReport = new XMLReort();
        List<String> data = List.of(
                "ID: 2, Type: DEPOSIT, Amount: 300, Sender: null, Receiver: 1003, Date: today"
        );
        String fileName = "test_report.xml";


        dispatcher.dispatch(
                () -> xmlReport.generate(fileName, data),
                "REPORT_GENERATED",
                "PDF report generated"
        );


        File file = new File(fileName);
        assertTrue(file.exists(), "XML file should be generated");
        assertTrue(file.length() > 0, "XML file should not be empty");

        assertEquals(1, audit.getLogs().size());

        file.delete();
    }
}
