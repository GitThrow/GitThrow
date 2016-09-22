package net.orekyuu.workbench.controller.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TicketCommentModel {

    private String userId;
    private String userName;
    private String text;
    private LocalDateTime createdAt;

    public TicketCommentModel(String userId, String userName, String text) {
        this.userId = userId;
        this.userName = userName;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
