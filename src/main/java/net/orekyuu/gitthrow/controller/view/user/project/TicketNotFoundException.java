package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.service.exceptions.ContentNotFoundException;

public class TicketNotFoundException extends ContentNotFoundException {
    public TicketNotFoundException(String projectId) {
        super(projectId);
    }
}
