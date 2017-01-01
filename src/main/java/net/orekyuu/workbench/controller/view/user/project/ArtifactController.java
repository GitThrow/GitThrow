package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.build.model.domain.Artifact;
import net.orekyuu.workbench.build.usecase.ArtifactUsecase;
import net.orekyuu.workbench.controller.exception.ResourceNotFoundException;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.service.exceptions.ContentNotFoundException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class ArtifactController {

    @Autowired
    private ArtifactUsecase artifactUsecase;
    private static final Logger logger = LoggerFactory.getLogger(ArtifactController.class);

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/files")
    public String showArtifactList(@ProjectName @PathVariable String projectId, Model model, Project project) {
        List<Artifact> artifactTables = artifactUsecase.findByProject(project);
        model.addAttribute("artifactTables", artifactTables);
        return "user/project/artifact-list";
    }

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/artifact/{artifactId}")
    public void downloadArtifact(@ProjectName @PathVariable String projectId,
                                 @PathVariable int artifactId,
                                 HttpServletResponse res) {
        Pair<Artifact, InputStream> pair = artifactUsecase.findById(artifactId).orElseThrow(ResourceNotFoundException::new);

        try {
            InputStream inputStream = pair.getSecond();

            res.setHeader("Content-Disposition", "attachment; filename=" + pair.getFirst().getFileName());
            res.setHeader("Content-Transfer-Encoding", "binary");
            res.setContentType("application/octet-stream;");
            ServletOutputStream outputStream = res.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ContentNotFoundException(projectId);
        }
    }

    @ProjectMemberOnly
    @PostMapping("/project/{projectId}/artifact/delete")
    public String deleteArtifact(@ProjectName @PathVariable String projectId, @RequestParam(value="artifactId") int artifactId, Project project) {
        artifactUsecase.delete(artifactId, project);
        return String.format("redirect:/project/%s/files", projectId);
    }
}
