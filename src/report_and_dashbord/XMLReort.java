package report_and_dashbord;

import java.io.FileWriter;
import java.util.List;

public class XMLReort  implements ReportGenerateStrategy{
    @Override
    public void generate(String fileName, List<String> summary) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("<report>\n");
            for (String line : summary) {
                writer.write("  <entry>" + line + "</entry>\n");
            }
            writer.write("</report>");
            System.out.println("XML report generated: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
