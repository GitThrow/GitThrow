package net.orekyuu.workbench.ticket.domain.model;

import java.util.Objects;

public class TicketStatus {
    private final int id;
    private final String name;

    public TicketStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TicketStatus of(int id, String name) {
        return new TicketStatus(id, name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketStatus that = (TicketStatus) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TicketStatus{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
