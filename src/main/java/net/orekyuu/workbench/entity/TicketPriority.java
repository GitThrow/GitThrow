package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "ticket_priority")
public class TicketPriority {

    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "project")
    public String project;
    @Column(name = "priority")
    public String priority;
}
