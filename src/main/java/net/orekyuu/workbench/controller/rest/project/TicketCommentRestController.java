package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.exception.ResourceNotFoundException;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.project.usecase.ProjectUsecase;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.domain.model.TicketComment;
import net.orekyuu.workbench.ticket.usecase.TicketCommentUsecase;
import net.orekyuu.workbench.ticket.usecase.TicketUsecase;
import net.orekyuu.workbench.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/project/{project}/ticket/{ticketId}/comment")
public class TicketCommentRestController {

    @Autowired
    private ProjectUsecase projectUsecase;
    @Autowired
    private TicketCommentUsecase commentUsecase;
    @Autowired
    private TicketUsecase ticketUsecase;
    @Autowired
    private UserUsecase userService;

    @GetMapping
    public List<TicketComment> show(@PathVariable("project") String projectId,
                                    @PathVariable("ticketId") int id,
                                    @AuthenticationPrincipal WorkbenchUserDetails principal) {
        Project project = projectUsecase.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!project.getMember().contains(principal.getUser())) {
            throw new NotMemberException();
        }

        Ticket ticket = ticketUsecase.findById(project, id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        return commentUsecase.findByTicket(ticket);
    }

    @PostMapping
    public TicketComment create(@PathVariable("project") String projectId,
                                @PathVariable("ticketId") int id,
                                @RequestBody TicketComment comment,
                                @AuthenticationPrincipal WorkbenchUserDetails principal) {
        Project project = projectUsecase.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!project.getMember().contains(principal.getUser())) {
            throw new NotMemberException();
        }

        Ticket ticket = ticketUsecase.findById(project, id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return commentUsecase.create(ticket, comment.getText(), principal.getUser());
    }
}
