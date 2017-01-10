package net.orekyuu.gitthrow.controller.rest.project;

import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.controller.exception.ResourceNotFoundException;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.ticket.domain.model.Ticket;
import net.orekyuu.gitthrow.ticket.usecase.TicketUsecase;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/{project}/ticket")
public class TicketRestController {

    @Autowired
    private ProjectUsecase projectUsecase;
    @Autowired
    private TicketUsecase ticketUsecase;
    @Autowired
    private UserUsecase userUsecase;

    @PutMapping("{ticketId}")
    public Ticket updateTicket(@PathVariable("ticketId") int id, Project project, @RequestBody Ticket ticket) {
        return ticketUsecase.update(new Ticket(project.getId(), id, ticket.getTitle(), ticket.getDescription(), ticket.getAssignee(), ticket.getProponent(), ticket.getLimit(), ticket.getType(), ticket.getStatus(), ticket.getPriority()));
    }

    @GetMapping
    public List<Ticket> showAll(Project project) {
        return ticketUsecase.findByProject(project);
    }

    @GetMapping("{ticketId}")
    public Ticket find(Project project, @PathVariable("ticketId") int id) {
        return ticketUsecase.findById(project, id)
            .orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping
    public Ticket create(Project project, @AuthenticationPrincipal WorkbenchUserDetails principal, @RequestBody Ticket ticket) {
        return ticketUsecase.create(
            project,
            ticket.getTitle(),
            ticket.getDescription(),
            ticket.getAssignee(),
            principal.getUser(),
            ticket.getLimit(),
            ticket.getType(),
            ticket.getStatus(),
            ticket.getPriority());
    }
}
