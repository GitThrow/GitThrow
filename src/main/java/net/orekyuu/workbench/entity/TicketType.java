package net.orekyuu.workbench.entity;

import org.seasar.doma.*;

@Entity
@Table(name = "ticket_type")
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int id;
    @Column(name = "project")
    public String project;
    @Column(name = "type")
    public String type;
}
