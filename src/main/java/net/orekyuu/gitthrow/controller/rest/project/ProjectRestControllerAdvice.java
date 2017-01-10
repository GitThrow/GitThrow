package net.orekyuu.gitthrow.controller.rest.project;

import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.controller.view.user.project.NotMemberException;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.service.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("net.orekyuu.gitthrow.controller.rest.project")
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
