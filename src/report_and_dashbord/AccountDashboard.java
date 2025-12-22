package report_and_dashbord;

import account.Account;
import account.inmemmory;

import java.util.ArrayList;
import java.util.List;

public class AccountDashboard {
    private inmemmory accountRepo;
    private ReportGenerateStrategy reportStrategy;

    public AccountDashboard(inmemmory repo, ReportGenerateStrategy strategy) {
        this.accountRepo = repo;
        this.reportStrategy = strategy;
    }

    public void displayDashboard() {
        System.out.println("====================================");
        System.out.println("      BANKING SYSTEM DASHBOARD      ");
        System.out.println("====================================");
        System.out.println("• Total Accounts in System:  " + accountRepo.allAccountCount());
        System.out.println("• New Accounts Created Today: " + accountRepo.todayAccountCount());
        System.out.println("• Recent Profile Updates:     " + accountRepo.todayAccountCountupdate());
        System.out.println("------------------------------------");
    }

    public List<String> getFullReport() {
        List<String> finalReport = new ArrayList<>();

        finalReport.add("================================");
        finalReport.add("   BANK ACCOUNTS DASHBOARD      ");
        finalReport.add("================================");
        finalReport.add("Total Accounts: " + accountRepo.allAccountCount());
        finalReport.add("New Today:      " + accountRepo.todayAccountCount());
        finalReport.add("Updated Today:  " + accountRepo.todayAccountCountupdate());
        finalReport.add("--------------------------------");

        finalReport.add("       ACCOUNTS DETAILS         ");
        finalReport.add("--------------------------------");

        for (Account acc : accountRepo.findAll().values()) {
            AccountReportAdapter adapter = new AccountReportAdapter(acc);
            finalReport.addAll(adapter.toReportData());
        }

        finalReport.add("================================");
        return finalReport;
    }
    public void exportReport(String fileName) {
        reportStrategy.generate(fileName, getFullReport());
        System.out.println("Report exported successfully using: " + reportStrategy.getClass().getSimpleName());
    }


    public void setReportStrategy(ReportGenerateStrategy strategy) {
        this.reportStrategy = strategy;
    }
}