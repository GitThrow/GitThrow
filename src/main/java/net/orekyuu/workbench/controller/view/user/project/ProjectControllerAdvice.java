package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.exceptions.ContentNotFoundException;
import net.orekyuu.workbench.service.exceptions.NotProjectMemberException;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice("net.orekyuu.workbench.controller.view.user.project")
public class ProjectControllerAdvice {

    @Autowired
    private ProjectService projectService;

    @ModelAttribute
    public void addProjectObject(@PathVariable("projectId") String projectId, Model model) throws ProjectNotFoundException {
        Project project = projectService.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        model.addAttribute("project", project);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProjectNotFound(ProjectNotFoundException e, Model model) {
        model.addAttribute("projectId", e.getProjectId());
        return "error/project_not_found";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleNotProjectMemberException(NotProjectMemberException e) {
        return "error/project_not_have_access_privileges";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleContentNotFoundException(ContentNotFoundException e, Model model) throws ProjectNotFoundException {
        Project project = projectService.findById(e.getProjectId()).orElseThrow(() -> new ProjectNotFoundException(e.getProjectId()));
        model.addAttribute("project", project);
        return "error/content_not_found";
    }
}
