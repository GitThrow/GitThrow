package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProjectDashboardController {

    @Autowired
    public ProjectService projectService;

    @GetMapping("/project/{projectId}")
    public String show(@PathVariable String projectId, Model model) {
        return "user/project/index";
    }
}
