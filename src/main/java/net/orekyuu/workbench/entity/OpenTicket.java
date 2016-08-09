package net.orekyuu.workbench.entity;


import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "open_tickets")
public class OpenTicket {
    @Column(name = "project")
    public String project;
    @Column(name = "ticket_num")
    public int ticketNum;
    @Column(name = "title")
    public String title;
    @Column(name = "description")
    public String description;
    @Column(name = "assignee")
    public String assignee;
    @Column(name = "proponent")
    public String proponent;
    @Column(name = "limit")
    public LocalDateTime limit;
    @Column(name = "type")
    public int type;
    @Column(name = "status")
    public int status;
    @Column(name = "priority")
    public int priority;
}
