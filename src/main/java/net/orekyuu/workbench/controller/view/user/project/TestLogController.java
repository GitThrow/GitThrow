package net.orekyuu.workbench.controller.view.user.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.orekyuu.workbench.build.model.domain.TestLog;
import net.orekyuu.workbench.build.usecase.TestLogUsecase;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.job.TestLogModel;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.service.exceptions.ContentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Controller
public class TestLogController {

    @Autowired
    private TestLogUsecase testLogUsecase;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/test")
    public String showTestLogList(@ProjectName @PathVariable String projectId, Model model, Project project) {
        List<TestLog> logs = testLogUsecase.findByProject(project);
        model.addAttribute("testLogs", logs);
        return "user/project/test-result-list";
    }

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/test/{id}")
    public String showTestLogDetail(@ProjectName @PathVariable String projectId, @PathVariable int id, Model model) throws IOException {
        TestLog testLog = testLogUsecase.findById(id)
            .filter(log -> log.getProjectId().equals(projectId))
            .orElseThrow(() -> new ContentNotFoundException(projectId));

        ObjectMapper mapper = new ObjectMapper();
        TestLogModel logModel = mapper.readValue(testLog.getLog(), TestLogModel.class);

        model.addAttribute("testLog", testLog);
        model.addAttribute("testLogModel", logModel);
        return "user/project/test-result-detail";
    }
}
