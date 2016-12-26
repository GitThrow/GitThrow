package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "ticket_type")
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final Long id;
    @Column(name = "project")
    private final String project;
    @Column(name = "type")
    private final String type;

    public TicketType(Long id, String project, String type) {
        this.id = id;
        this.project = project;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getProject() {
        return project;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketType that = (TicketType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
