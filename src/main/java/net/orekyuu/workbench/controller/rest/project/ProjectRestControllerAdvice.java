package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.project.usecase.ProjectUsecase;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("net.orekyuu.workbench.controller.rest.project")
public class ProjectRestControllerAdvice {

    @Autowired
    private ProjectUsecase usecase;

    @ModelAttribute
    public void project(@PathVariable("project") String projectId, @AuthenticationPrincipal WorkbenchUserDetails principal, Model model) {
        Project project = usecase.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (!project.getMember().contains(principal.getUser())) {
            throw new NotMemberException();
        }

        model.addAttribute("project", project);
    }
}
