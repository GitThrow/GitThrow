package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.RemoteRepositoryService;
import net.orekyuu.workbench.service.UserService;
import net.orekyuu.workbench.service.WorkspaceService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

@Controller
public class ProjectDashboardController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private RemoteRepositoryService remoteRepositoryService;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}")
    public String show(@ProjectName @PathVariable String projectId, Model model) throws ProjectNotFoundException, GitAPIException {
        Object project = model.asMap().get("project");
        if (project == null || !(project instanceof Project)) {
            throw new ProjectNotFoundException(projectId);
        }

        Project prj = ((Project) project);
        List<User> member = projectService.findProjectMember(projectId);
        User admin = member.stream()
            .filter(u -> Objects.equals(u.id, prj.ownerUserId)).findFirst()
            .orElseThrow(() -> new UsernameNotFoundException(prj.id));

        String readme = remoteRepositoryService.getReadmeFile(projectId).orElse("");

        model.addAttribute("readme", readme);
        model.addAttribute("member", member);
        model.addAttribute("admin", admin);

        return "user/project/index";
    }
}
