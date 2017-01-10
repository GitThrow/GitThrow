package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.infra.ProjectMemberOnly;
import net.orekyuu.gitthrow.infra.ProjectName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProjectSettingController {

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/settings")
    public String show(@ProjectName @PathVariable String projectId) {
        return "user/project/project-setting";
    }
}
