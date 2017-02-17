package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.service.exceptions.ContentNotFoundException;
import net.orekyuu.gitthrow.service.exceptions.NotProjectMemberException;
import net.orekyuu.gitthrow.service.exceptions.ProjectNotFoundException;
import net.orekyuu.gitthrow.theme.domain.model.ThemeImage;
import net.orekyuu.gitthrow.theme.port.ProjectThemeImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice("net.orekyuu.gitthrow.controller.view.user.project")
public class ProjectControllerAdvice {

    @Autowired
    private ProjectUsecase usecase;
    @Autowired
    private ProjectThemeImageRepository themeImageRepository;

    @ModelAttribute
    public void addProjectObject(@PathVariable("projectId") String projectId, Model model) throws ProjectNotFoundException {
        Project project = usecase.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        model.addAttribute("project", project);

        double opacity = themeImageRepository.findByProject(projectId).map(ThemeImage::getOpacity).orElse((double) 1);
        model.addAttribute("opacity", opacity);
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
        Project project = usecase.findById(e.getProjectId()).orElseThrow(() -> new ProjectNotFoundException(e.getProjectId()));
        model.addAttribute("project", project);
        return "error/content_not_found";
    }
}
