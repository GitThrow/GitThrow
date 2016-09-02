package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.Artifact;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ArtifactController {

    @Autowired
    private ArtifactService artifactService;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/files")
    public String showArtifactList(@ProjectName @PathVariable String projectId, Model model) {
        List<Artifact> artifacts = artifactService.findByProjectId(projectId);
        model.addAttribute("artifacts", artifacts);
        return "user/project/artifact-list";
    }
}
