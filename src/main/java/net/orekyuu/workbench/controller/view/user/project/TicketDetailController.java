package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.*;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.*;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class TicketDetailController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketTypeService typeService;
    @Autowired
    private TicketStatusService statusService;
    @Autowired
    private TicketPriorityService priorityService;
    @Autowired
    private ProjectService projectService;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/ticket/{ticketNum}")
    public String showDetail(@ProjectName @PathVariable String projectId, @PathVariable int ticketNum, Model model) throws ProjectNotFoundException {
        OpenTicket ticket = ticketService.findByProjectAndNum(projectId, ticketNum).orElseThrow(TicketNotFoundException::new);

        TicketModel ticketModel = toTicketModel(ticket, model);
        model.addAttribute("ticket", ticketModel);
        setupModel(model, projectId);

        return "user/project/ticket-detail";
    }

    @ModelAttribute("typeList")
    public List<TicketType> typeList(@PathVariable String projectId) {
        return typeService.findByProject(projectId);
    }

    @ModelAttribute("statusList")
    public List<TicketStatus> statusList(@PathVariable String projectId) {
        return statusService.findByProject(projectId);
    }

    @ModelAttribute("priorityList")
    public List<TicketPriority> priorityList(@PathVariable String projectId) {
        return priorityService.findByProject(projectId);
    }

    @ModelAttribute("member")
    public List<User> member(@PathVariable String projectId) throws ProjectNotFoundException {
        return projectService.findProjectMember(projectId);
    }

    private TicketModel toTicketModel(OpenTicket ticket, Model model) {
        Map<String, Object> objectMap = model.asMap();

        List<TicketType> typeList = (List<TicketType>) objectMap.get("typeList");
        List<TicketStatus> statusList = (List<TicketStatus>) objectMap.get("statusList");
        List<TicketPriority> priorityList = (List<TicketPriority>) objectMap.get("priorityList");

        String type = typeList.stream().filter(t -> t.id == ticket.type).map(t -> t.type).findFirst().orElseThrow(IllegalArgumentException::new);
        String status = statusList.stream().filter(t -> t.id == ticket.status).map(t -> t.status).findFirst().orElseThrow(IllegalArgumentException::new);
        String priority = priorityList.stream().filter(t -> t.id == ticket.priority).map(t -> t.priority).findFirst().orElseThrow(IllegalArgumentException::new);

        return new TicketModel(ticket.ticketNum, ticket.title, ticket.description, type, priority, status, ticket.limit, ticket.assignee, ticket.proponent);
    }

    private void setupModel(Model model, String projectId) throws ProjectNotFoundException {
        List<TicketType> typeList = typeService.findByProject(projectId);
        List<TicketStatus> statusList = statusService.findByProject(projectId);
        List<TicketPriority> priorityList = priorityService.findByProject(projectId);
        List<User> member = projectService.findProjectMember(projectId);

        model.addAttribute("typeList", typeList);
        model.addAttribute("statusList", statusList);
        model.addAttribute("priorityList", priorityList);
        model.addAttribute("member", member);
    }
}
