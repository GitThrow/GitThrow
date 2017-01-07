package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.exception.ResourceNotFoundException;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.domain.model.TicketComment;
import net.orekyuu.workbench.ticket.usecase.TicketCommentUsecase;
import net.orekyuu.workbench.ticket.usecase.TicketUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/{project}/ticket/{ticketId}/comment")
public class TicketCommentRestController {

    @Autowired
    private TicketCommentUsecase commentUsecase;
    @Autowired
    private TicketUsecase ticketUsecase;

    @GetMapping
    public List<TicketComment> show(Project project, @PathVariable("ticketId") int id) {
        Ticket ticket = ticketUsecase.findById(project, id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        return commentUsecase.findByTicket(ticket);
    }

    @PostMapping
    public TicketComment create(Project project,
                                @PathVariable("ticketId") int id,
                                @RequestBody TicketComment comment,
                                @AuthenticationPrincipal WorkbenchUserDetails principal) {
        Ticket ticket = ticketUsecase.findById(project, id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return commentUsecase.create(ticket, comment.getText(), principal.getUser());
    }
}
