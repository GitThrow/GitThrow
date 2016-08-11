package net.orekyuu.workbench.service;

public class TicketModel {

    private int number;
    private String title;
    private String type;
    private String priority;
    private String status;

    public TicketModel(int number, String title, String type, String priority, String status) {
        this.number = number;
        this.title = title;
        this.type = type;
        this.priority = priority;
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }
}
