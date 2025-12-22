package report_and_dashbord;

import account.Account;
import java.util.List;

public class AccountReportAdapter implements Reportable {
    private Account account;

    public AccountReportAdapter(Account account) {
        this.account = account;
    }

    @Override
    public List<String> toReportData() {
        // تحويل بيانات الحساب الموجودة في ملف Account.java إلى قائمة نصوص
        return List.of(
                "Account ID: " + account.getAccount_id() +
                        ", Owner: " + account.getName() +
                        ", Balance: " + account.getBalance() +
                        ", State: " + (account.getState() != null ? account.getState().getName() : "N/A")
        );
    }
}