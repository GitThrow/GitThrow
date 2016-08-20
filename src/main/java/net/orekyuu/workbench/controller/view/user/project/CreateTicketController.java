package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CreateTicketController {

    @Autowired
    private TicketService ticketService;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/ticket/create")
    public String show(@ProjectName @PathVariable String projectId, Model model) {
        return "user/project/new-ticket";
    }
}
