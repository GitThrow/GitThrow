package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.controller.view.user.project.TicketNotFoundException;
import net.orekyuu.workbench.entity.OpenTicket;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/ticket")
public class TicketRestController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private ProjectService projectService;

    @PostMapping
    @ResponseBody
    public OpenTicket updateTicket(@RequestBody TicketUpdateRequest req, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(req.getProject(), principal.getUser().id)) {
            throw new NotMemberException();
        }

        OpenTicket ticket = ticketService.findByProjectAndNum(req.getProject(), req.getTicketNum())
            .orElseThrow(() -> new TicketNotFoundException(req.getProject()));
        ticket.title = req.getTitle();
        ticket.description = req.getDescription();
        ticket.limit = req.getLimit() == null ? null : req.getLimit().atStartOfDay();
        ticket.assignee = req.getAssignee();
        ticket.type = req.getType();
        ticket.status = req.getStatus();
        ticket.priority = req.getPriority();
        ticketService.update(ticket);
        return ticket;
    }

    @GetMapping("/all")
    public List<OpenTicket> showAll(@RequestParam("project") String projectId, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(projectId, principal.getUser().id)) {
            throw new NotMemberException();
        }
        return ticketService.findOpenTicketByProject(projectId);
    }

    @GetMapping("/assignee")
    public List<OpenTicket> showRelated(@RequestParam("project") String projectId, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(projectId, principal.getUser().id)) {
            throw new NotMemberException();
        }
        return ticketService.findTicketByProjectAndAssignee(projectId, principal.getUser().id);
    }
}
