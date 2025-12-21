package Ticket;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketRepository {

    private List<Ticket> tickets = new ArrayList<>();
    private List<LocalDate> createdDates = new ArrayList<>();

    public void save(Ticket ticket) {
        tickets.add(ticket);
        createdDates.add(LocalDate.now());
    }


    public int getTotalTickets() {
        return tickets.size();
    }


    public int getTodayTicketsCount() {
        LocalDate today = LocalDate.now();
        int count = 0;
        for (LocalDate date : createdDates) {
            if (date.equals(today)) {
                count++;
            }
        }
        return count;
    }

    public List<Ticket> filterByStatus(String status) {
        return tickets.stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }


    public List<Ticket> filterByType(Tickettype type) {
        return tickets.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Ticket> findAll() {
        return tickets;
    }
}
