package report_and_dashbord;

import Transaction.Transaction;
import Transaction.TransactionRepository;
import java.util.ArrayList;
import java.util.List;

public class TransactionDashboard {
    private TransactionRepository repository;
    private ReportGenerateStrategy reportStrategy;

    public TransactionDashboard(TransactionRepository repository, ReportGenerateStrategy strategy) {
        this.repository = repository;
        this.reportStrategy = strategy;
    }

    public List<String> getFullTransactionReportData() {
        List<String> report = new ArrayList<>();

        report.add("====================================");
        report.add("      TRANSACTIONAL DASHBOARD       ");
        report.add("====================================");
        report.add("• Total Transactions:  " + repository.getTotalTransactionsCount());
        report.add("• Transactions Today:  " + repository.getTodayTransactionsCount());
        report.add("• Total Volume Today:  $" + repository.getTodayTotalAmount());
        report.add("------------------------------------");

        report.add("       RECENT TRANSACTIONS LIST      ");
        report.add("------------------------------------");

        for (Transaction tx : repository.findAll()) {
            // استخدام الـ Adapter الذي صممناه سابقاً
            TransactionReportAdapter adapter = new TransactionReportAdapter(tx);
            report.addAll(adapter.toReportData());
        }

        report.add("====================================");
        return report;
    }

    public void generateFinalReport(String fileName) {
        List<String> data = getFullTransactionReportData();
        reportStrategy.generate(fileName, data);
    }
}