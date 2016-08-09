package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "ticket_number")
public class TicketNum {

    @Id
    @Column(name = "project")
    public String project;
    @Column(name = "ticket_count")
    public int ticketCount;
}
