package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.service.exceptions.ContentNotFoundException;

public class TicketNotFoundException extends ContentNotFoundException {
    public TicketNotFoundException(String projectId) {
        super(projectId);
    }
}
