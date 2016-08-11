package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.service.TicketModel;
import net.orekyuu.workbench.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TicketListController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/project/{projectId}/ticket")
    public String showTicketList(@PathVariable String projectId, Model model) {
        List<TicketModel> models = ticketService.findOpenTicketModelByProject(projectId);

        model.addAttribute("ticketList", models);
        return "user/project/ticket-list";
    }
}
