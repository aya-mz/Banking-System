package Ticket;

import Ticket.GeneralInquiryStrategy;
import Ticket.*;
import Transaction.dispatcher.TransactionDispatcher;
import Transaction.observer.AuditLogObserver;
import Transaction.observer.NotificationObserver;
import Transaction.observer.TransactionEventPublisher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void testGeneralInquiryProcessing() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        Ticket ticket = new Ticket(1, Tickettype.GEN,
                "What is my account balance?", new GeneralInquiryStrategy());

        dispatcher.dispatch(
                () -> ticket.process(),
                "TICKET_PROCESS",
                "Ticket process has been "+ticket.getResponse()
        );

        assertEquals("resolved", ticket.getStatus());
        assertEquals("Thank you for your inquiry. Your question has been addressed.", ticket.getResponse());
        assertEquals(1, audit.getLogs().size());
    }

    @Test
    void testTechnicalIssueProcessing() {
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        AuditLogObserver audit = new AuditLogObserver();
        publisher.subscribe(audit);

        TransactionDispatcher dispatcher = new TransactionDispatcher(publisher);

        Ticket ticket = new Ticket(2, Tickettype.TEC,
                "Cannot log in.", new TechnicalIssueStrategy());

        dispatcher.dispatch(
                () -> ticket.process(),
                "TICKET_PROCESS",
                "Ticket process has been "+ticket.getResponse()
        );

        assertEquals("in_progress", ticket.getStatus());
        assertEquals("Your technical issue has been forwarded to our IT team.", ticket.getResponse());
        assertEquals(1, audit.getLogs().size());
    }
}
