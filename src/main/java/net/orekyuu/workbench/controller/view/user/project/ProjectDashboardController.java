package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.git.domain.RemoteRepository;
import net.orekyuu.workbench.git.domain.RemoteRepositoryFactory;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.project.usecase.ProjectUsecase;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.user.domain.model.User;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProjectDashboardController {

    @Autowired
    private ProjectUsecase projectUsecase;
    @Autowired
    private RemoteRepositoryFactory repositoryFactory;


    @ProjectMemberOnly
    @GetMapping("/project/{projectId}")
    public String show(@ProjectName @PathVariable String projectId, Model model) throws ProjectNotFoundException, GitAPIException {
        Object project = model.asMap().get("project");
        if (project == null || !(project instanceof Project)) {
            throw new ProjectNotFoundException(projectId);
        }

        Project prj = ((Project) project);
        List<User> member = prj.getMember();
        User admin = prj.getOwner();

        RemoteRepository repository = repositoryFactory.create(projectId);
        String readme = repository.getReadmeFile().orElse("");

        model.addAttribute("readme", readme);
        model.addAttribute("member", member);
        model.addAttribute("admin", admin);

        return "user/project/index";
    }
}
