package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.Artifact;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.ArtifactService;
import net.orekyuu.workbench.service.exceptions.ContentNotFoundException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/artifact/{artifactId}")
    public void downloadArtifact(@ProjectName @PathVariable String projectId, @PathVariable int artifactId, HttpServletResponse res) {
        Artifact artifact = artifactService.findById(artifactId)
            .filter(a -> a.projectId.equals(projectId))
            .orElseThrow(ContentNotFoundException::new);

        try {
            InputStream inputStream = artifactService.openArtifactStreamByArtifact(artifact);

            res.setHeader("Content-Disposition", "attachment; filename=" + artifact.fileName);
            res.setHeader("Content-Transfer-Encoding", "binary");
            res.setContentType("application/octet-stream;");
            ServletOutputStream outputStream = res.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ContentNotFoundException();
        }
    }
}
