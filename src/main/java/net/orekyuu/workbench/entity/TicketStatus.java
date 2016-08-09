package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "ticket_status")
public class TicketStatus {

    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "project")
    public String project;
    @Column(name = "status")
    public String status;
}
