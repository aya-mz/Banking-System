package Ticket;

public class TechnicalIssueStrategy implements TicketStrategy {
    @Override
    public void handleTicket(Ticket ticket) {
        ticket.setResponse("Your technical issue has been forwarded to our IT team.");
        ticket.setStatus("in_progress");
    }
}
