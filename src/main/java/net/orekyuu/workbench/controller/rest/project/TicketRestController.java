package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.controller.view.user.project.TicketNotFoundException;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.port.table.OpenTicketTable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/ticket")
public class TicketRestController {

    @PostMapping
    @ResponseBody
    public Ticket updateTicket(@RequestBody TicketUpdateRequest req, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(req.getProject(), principal.getUser().id)) {
            throw new NotMemberException();
        }

        OpenTicketTable ticket = ticketService.findByProjectAndNum(req.getProject(), req.getTicketNum())
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
    public List<OpenTicketTable> showAll(@RequestParam("project") String projectId, @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(projectId, principal.getUser().id)) {
            throw new NotMemberException();
        }
        return ticketService.findOpenTicketByProject(projectId);
    }
}
