package net.orekyuu.workbench.controller.rest.project;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class TicketUpdateRequest {

    private String project;
    private int ticketNum;
    private String title;
    private String description;
    private String assignee;
    private String proponent;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate limit;
    private int type;
    private int status;
    private int priority;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(int ticketNum) {
        this.ticketNum = ticketNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getProponent() {
        return proponent;
    }

    public void setProponent(String proponent) {
        this.proponent = proponent;
    }

    public LocalDate getLimit() {
        return limit;
    }

    public void setLimit(LocalDate limit) {
        this.limit = limit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
