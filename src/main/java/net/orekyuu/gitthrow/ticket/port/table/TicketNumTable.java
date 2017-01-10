package net.orekyuu.gitthrow.ticket.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "ticket_number")
public class TicketNumTable {

    @Id
    @Column(name = "project")
    private final String project;
    @Column(name = "ticket_count")
    private final Long ticketCount;

    public TicketNumTable(String project, Long ticketCount) {
        this.project = project;
        this.ticketCount = ticketCount;
    }

    public String getProject() {
        return project;
    }

    public Long getTicketCount() {
        return ticketCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketNumTable ticketNumTable = (TicketNumTable) o;
        return Objects.equals(project, ticketNumTable.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project);
    }
}
