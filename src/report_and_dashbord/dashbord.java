package report_and_dashbord;

import Ticket.TicketRepository;

public class dashbord {
ReportGenerateStrategy reportGenerateStrategy ;
public  void dashbord(ReportGenerateStrategy reportGenerateStrategy, TicketRepository repository){
    this.repository = repository;

    this.reportGenerateStrategy = reportGenerateStrategy ;
}
    private TicketRepository repository;


    public int totalTickets() {
        return repository.getTotalTickets();
    }

    public int todayTickets() {
        return repository.getTodayTicketsCount();
    }

    public int openTickets() {
        return repository.filterByStatus("open").size();
    }

    public int closedTickets() {
        return repository.filterByStatus("closed").size();
    }
}
