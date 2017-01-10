package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.infra.ProjectMemberOnly;
import net.orekyuu.gitthrow.infra.ProjectName;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.ticket.domain.model.Ticket;
import net.orekyuu.gitthrow.ticket.usecase.TicketUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TicketListController {

    @Autowired
    private TicketUsecase ticketService;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/ticket")
    public String showTicketList(@ProjectName @PathVariable String projectId, Model model, Project project) {
        List<Ticket> tickets = ticketService.findByProject(project);

        model.addAttribute("ticketList", tickets);
        return "user/project/ticket-list";
    }
}
