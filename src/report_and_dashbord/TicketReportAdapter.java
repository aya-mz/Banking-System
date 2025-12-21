package report_and_dashbord ;

import Ticket.Ticket;
import report_and_dashbord.Reportable;
import java.util.List;

public class TicketReportAdapter implements Reportable {

    private Ticket ticket;

    public TicketReportAdapter(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public List<String> toReportData() {
        return List.of(
                "ID: " + ticket.getId() +
                        ", User: " + ticket.getUserId() +
                        ", Type: " + ticket.getType() +
                        ", Status: " + ticket.getStatus() +
                        ", Description: " + ticket.getDescription()
        );
    }
}
