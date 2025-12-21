package report_and_dashbord;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerateReportTest {

    @Test
    void testGeneratePDFReport() {

        ReportGenerateStrategy pdfReport = new PDFReport();
        List<String> data = List.of(
                "ID: 1, Type: TRANSFER, Amount: 500, Sender: 1001, Receiver: 1002, Date: today"
        );
        String fileName = "test_report.pdf";


        pdfReport.generate(fileName, data);


        File file = new File(fileName);
        assertTrue(file.exists(), "PDF file should be generated");
        assertTrue(file.length() > 0, "PDF file should not be empty");


        file.delete();
    }

    @Test
    void testGenerateXMLReport() {

        ReportGenerateStrategy xmlReport = new XMLReort();
        List<String> data = List.of(
                "ID: 2, Type: DEPOSIT, Amount: 300, Sender: null, Receiver: 1003, Date: today"
        );
        String fileName = "test_report.xml";


        xmlReport.generate(fileName, data);


        File file = new File(fileName);
        assertTrue(file.exists(), "XML file should be generated");
        assertTrue(file.length() > 0, "XML file should not be empty");


        file.delete();
    }
}
