package report_and_dashbord;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

public class PDFReport implements ReportGenerateStrategy {

    @Override
    public void generate(String fileName, List<String> summary) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            document.add(new Paragraph("System Report"));
            document.add(new Paragraph(" "));

            for (String line : summary) {
                document.add(new Paragraph(line));
            }

            document.close();
            System.out.println("PDF generated: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
