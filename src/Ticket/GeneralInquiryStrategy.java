package Ticket;

public class GeneralInquiryStrategy implements TicketStrategy {
    @Override
    public void handleTicket(Ticket ticket) {
        ticket.setResponse("Thank you for your inquiry. Your question has been addressed.");
        ticket.setStatus("resolved");
    }
}
