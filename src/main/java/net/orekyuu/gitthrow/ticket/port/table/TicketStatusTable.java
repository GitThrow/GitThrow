package net.orekyuu.gitthrow.ticket.port.table;

import org.seasar.doma.*;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "ticket_status")
public class TicketStatusTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final Long id;
    @Column(name = "project")
    private final String project;
    @Column(name = "status")
    private final String status;

    public TicketStatusTable(Long id, String project, String status) {
        this.id = id;
        this.project = project;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getProject() {
        return project;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketStatusTable that = (TicketStatusTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
