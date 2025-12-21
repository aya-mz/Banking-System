package Ticket;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void testGeneralInquiryProcessing() {

        Ticket ticket = new Ticket(101, Tickettype.GEN,
                "What is my account balance?", new GeneralInquiryStrategy());


        ticket.process();


        assertEquals("resolved", ticket.getStatus());
        assertEquals("Thank you for your inquiry. Your question has been addressed.", ticket.getResponse());
    }

    @Test
    void testTechnicalIssueProcessing() {
        Ticket ticket = new Ticket(102, Tickettype.TEC,
                "Cannot log in.", new TechnicalIssueStrategy());

        ticket.process();

        assertEquals("in_progress", ticket.getStatus());
        assertEquals("Your technical issue has been forwarded to our IT team.", ticket.getResponse());
    }
}
