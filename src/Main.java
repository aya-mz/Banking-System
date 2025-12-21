
import Transaction.*;
import account.Account;
import account.AccountType;
import core.user.UserTest;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserTest userTest=new UserTest();
        userTest.test1();
        List<Transaction> transactions = new ArrayList<>();
        Account account = new Account(1,"sd" , AccountType.CHECKING,200,0);
        Account account2 = new Account(2,"sdss" , AccountType.CHECKING,200,0);
        Account account3 = new Account(6,"sddf" , AccountType.CHECKING,200,0);

        transactions.add(new Transaction(100, 2, account, account2, TransactionType.TRANSFER ));
        transactions.add(new Transaction(100, 2, account, account3, TransactionType.TRANSFER ));


//        List<String> reportData = new ArrayList<>();
//        for (Transaction t : transactions) {
//            reportData.addAll(t());
//        }

        // -------------------------------
        // PDF Test
        // -------------------------------
//        report_and_dashbord.ReportGenerateStrategy pdfReport = new report_and_dashbord.PDFReport();
//        pdfReport.generate("transactions_test.pdf", reportData);

        // -------------------------------
        // XML Test
        // -------------------------------
//        report_and_dashbord.ReportGenerateStrategy xmlReport = new report_and_dashbord.XMLReort();
//        xmlReport.generate("transactions_test.xml", reportData);

//        System.out.println("PDF and XML test completed!");

}}