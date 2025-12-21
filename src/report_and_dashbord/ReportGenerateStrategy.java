package report_and_dashbord;

import java.util.List;

public interface ReportGenerateStrategy {
    public void generate(String fileName , List<String> summary);
}
