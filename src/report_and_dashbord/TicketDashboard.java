package report_and_dashbord;

import Ticket.Ticket;
import Ticket.TicketRepository;
import java.util.ArrayList;
import java.util.List;

public class TicketDashboard {
    private TicketRepository ticketRepository;
    private ReportGenerateStrategy reportStrategy;

    public TicketDashboard(TicketRepository repository, ReportGenerateStrategy strategy) {
        this.ticketRepository = repository;
        this.reportStrategy = strategy;
    }

    // الدالة التي طلبتها: تجمع الإحصائيات مع الملخص في List<String>
    public List<String> getTicketsFullSummary() {
        List<String> report = new ArrayList<>();

        // 1. إضافة إحصائيات التذاكر (Stats)
        report.add("====================================");
        report.add("      TICKETS SYSTEM DASHBOARD      ");
        report.add("====================================");
        report.add("• Total Tickets:    " + ticketRepository.getTotalTickets());
        report.add("• Today's Tickets:  " + ticketRepository.getTodayTicketsCount());
        report.add("• Open Tickets:     " + ticketRepository.filterByStatus("open").size());
        report.add("• Closed Tickets:   " + ticketRepository.filterByStatus("closed").size());
        report.add("------------------------------------");

        // 2. إضافة ملخص كل تذكرة (Summary باستخدام الـ Adapter)
        report.add("        DETAILED TICKETS LIST        ");
        report.add("------------------------------------");

        for (Ticket ticket : ticketRepository.findAll()) {
            TicketReportAdapter adapter = new TicketReportAdapter(ticket);
            report.addAll(adapter.toReportData());
        }

        report.add("====================================");
        return report;
    }

    public void exportTicketReport(String fileName) {
        List<String> data = getTicketsFullSummary();
        reportStrategy.generate(fileName, data);
    }
}