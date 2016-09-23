package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.TestLog;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.TestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TestLogController {

    @Autowired
    private TestLogService testLogService;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/test")
    public String showTestLogList(@ProjectName @PathVariable String projectId, Model model) {
        List<TestLog> logs = testLogService.findByProject(projectId);
        model.addAttribute("testLogs", logs);
        return "user/project/test-result-list";
    }
}
