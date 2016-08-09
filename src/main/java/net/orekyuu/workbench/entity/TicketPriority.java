package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

@Entity
@Table(name = "ticket_priority")
public class TicketPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;
    @Column(name = "project")
    public String project;
    @Column(name = "priority")
    public String priority;
}
