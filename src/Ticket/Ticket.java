package Ticket;

import java.util.logging.Logger;

public class Ticket {
    private static int counter = 1;
    private int id;
    private int userId;
    private Tickettype type;
    private String description;
    private String status;
    private String response;

    private TicketStrategy strategy;
    private static final Logger logger = Logger.getLogger(Ticket.class.getName());

    public Ticket(int userId, Tickettype type, String description, TicketStrategy strategy) {
        this.id = counter++;
        this.userId = userId;
        this.type = type;
        this.description = description;
        this.status = "new";
        this.response = "";
        this.strategy = strategy;
    }

    // getters and setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public Tickettype getType() { return type; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }


    public void process() {
        strategy.handleTicket(this);
        logger.info("Ticket #" + id + " processed: " + response + " | Status: " + status);

        System.out.println("Ticket #" + id + " processed: " + response + " | Status: " + status);
    }
}
