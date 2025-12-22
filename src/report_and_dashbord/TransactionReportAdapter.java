package report_and_dashbord;

import Transaction.Transaction;
import java.util.List;

public class TransactionReportAdapter implements Reportable {
    private Transaction transaction;

    public TransactionReportAdapter(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public List<String> toReportData() {
        // تحويل بيانات المعاملة لسطر واحد مفصل
        String senderName = (transaction.getSenderaccount() != null) ? transaction.getSenderaccount().getName() : "System";
        String receiverName = (transaction.getReciveaccount() != null) ? transaction.getReciveaccount().getName() : "System";

        return List.of(
                "TX_ID: " + transaction.getTransaction_id() +
                        " | Type: " + transaction.getType() +
                        " | Amount: $" + transaction.getAmount() +
                        " | From: " + senderName +
                        " | To: " + receiverName
        );
    }
}