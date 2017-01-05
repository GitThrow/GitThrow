package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.usecase.TicketUsecase;
import net.orekyuu.workbench.user.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TicketDetailController {

    @Autowired
    private TicketUsecase ticketUsecase;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/ticket/{ticketNum}")
    public String showDetail(@ProjectName @PathVariable String projectId, @PathVariable int ticketNum, Model model, Project project) throws ProjectNotFoundException {
        Ticket ticket = ticketUsecase.findById(project, ticketNum).orElseThrow(() -> new TicketNotFoundException(projectId));
        model.addAttribute("ticket", ticket);
        setupModel(model, project);

        return "user/project/ticket-detail";
    }

    @ModelAttribute("member")
    public List<User> member(Project project) throws ProjectNotFoundException {
        return project.getMember();
    }


    private void setupModel(Model model, Project project) throws ProjectNotFoundException {
        model.addAttribute("typeList", ticketUsecase.findTypeByProject(project));
        model.addAttribute("statusList", ticketUsecase.findStatusByProject(project));
        model.addAttribute("priorityList", ticketUsecase.findPriorityByProject(project));
    }
}
