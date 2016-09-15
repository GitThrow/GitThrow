package net.orekyuu.workbench.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketModel {

    private int number;
    private String title;
    private String type;
    private String priority;
    private String status;
    private String desc;
    private String limit;
    private String assignUserId;
    private String proponentUserId;

    public TicketModel(int number, String title, String desc, String type, String priority, String status, LocalDateTime limit, String assignUserId, String proponentUserId) {
        this.number = number;
        this.title = title;
        this.type = type;
        this.priority = priority;
        this.status = status;
        this.desc = desc;
        this.limit = limit == null ? "無し" : limit.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        this.assignUserId = assignUserId;
        this.proponentUserId = proponentUserId;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLimit() {
        return limit;
    }

    public String getAssignUserId() {
        return assignUserId;
    }

    public String getProponentUserId() {
        return proponentUserId;
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

    @Override
    public String toString() {
        return "TicketModel{" +
            "number=" + number +
            ", title='" + title + '\'' +
            ", type='" + type + '\'' +
            ", priority='" + priority + '\'' +
            ", status='" + status + '\'' +
            ", desc='" + desc + '\'' +
            ", limit='" + limit + '\'' +
            ", assignUserId='" + assignUserId + '\'' +
            ", proponentUserId='" + proponentUserId + '\'' +
            '}';
    }
}
