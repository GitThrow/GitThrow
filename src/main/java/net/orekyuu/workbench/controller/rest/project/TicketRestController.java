package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.exception.ResourceNotFoundException;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.project.usecase.ProjectUsecase;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.usecase.TicketUsecase;
import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/rest/ticket")
public class TicketRestController {

    @Autowired
    private ProjectUsecase projectUsecase;
    @Autowired
    private TicketUsecase ticketUsecase;
    @Autowired
    private UserUsecase userUsecase;

    @PostMapping
    @ResponseBody
    public Ticket updateTicket(@RequestBody TicketUpdateRequest req, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        Project project = projectUsecase.findById(req.getProject()).orElseThrow(() -> new ProjectNotFoundException(req.getProject()));

        if (!project.getMember().contains(principal.getUser())) {
            throw new NotMemberException();
        }


        User assignee = req.getAssignee() == null ? null : userUsecase.findById(req.getAssignee()).filter(u -> project.getMember().contains(u)).orElse(null);
        User proponent = req.getProponent() == null ? null : userUsecase.findById(req.getProponent()).filter(u -> project.getMember().contains(u)).orElse(null);
        LocalDateTime limit = req.getLimit() != null ? req.getLimit().atStartOfDay() : null;

        Ticket ticket = new Ticket(
            req.getProject(),
            req.getTicketNum(),
            req.getTitle(),
            req.getDescription(),
            assignee,
            proponent,
            limit,
            ticketUsecase.findTypeById(project, req.getType()).orElseThrow(() -> new ResourceNotFoundException("Ticket type")),
            ticketUsecase.findStatusById(project, req.getStatus()).orElseThrow(() -> new ResourceNotFoundException("Ticket status")),
            ticketUsecase.findPriorityById(project, req.getPriority()).orElseThrow(() -> new ResourceNotFoundException("Ticket priority"))
            );
        return ticketUsecase.update(ticket);
    }

    @GetMapping("/all")
    public List<Ticket> showAll(@RequestParam("project") String projectId, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        Project project = projectUsecase.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!project.getMember().contains(principal.getUser())) {
            throw new NotMemberException();
        }

        return ticketUsecase.findByProject(project);
    }
}
