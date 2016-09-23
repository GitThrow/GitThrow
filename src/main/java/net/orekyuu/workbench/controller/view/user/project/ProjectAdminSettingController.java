package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.infra.ProjectOwnerOnly;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Controller
public class ProjectAdminSettingController {

    @Autowired
    private ProjectService projectService;

    private static final Logger logger = LoggerFactory.getLogger(ProjectAdminSettingController.class);
    @ModelAttribute("projectMember")
    public List<User> projectMember(@PathVariable String projectId) throws ProjectNotFoundException {
        List<User> projectMember = projectService.findProjectMember(projectId);
        return projectMember;
    }

    @ProjectOwnerOnly
    @GetMapping("/project/{projectId}/admin-settings")
    public String show(@ProjectName @PathVariable String projectId) {
        return "user/project/admin-setting";
    }

    //メンバーの削除
    @ProjectOwnerOnly
    @PostMapping(value = "/project/{projectId}/admin-settings/member/delete", params = "delete")
    public String deleteMember(@ProjectName @PathVariable String projectId, @RequestParam String delete) throws ProjectNotFoundException {
        Project project = projectService.findById(projectId).get();
        if (project.ownerUserId.equals(delete)) {
            logger.info("プロジェクト管理者を削除しようとしたのでスキップ");
        } else {
            projectService.withdrawProject(projectId, delete);
        }
        return "redirect:/project/" + projectId + "/admin-settings";
    }

    @PostMapping(value = "/project/{projectId}/admin-settings/member/new")
    @ProjectOwnerOnly
    public String newMember(@ProjectName @PathVariable String projectId, @RequestParam("newMemberId") String newMemberId) {
        if (projectService.isJoined(projectId, newMemberId)) {
            logger.info("すでに参加済み");
        } else {
            projectService.joinToProject(projectId, newMemberId);
        }
        return "redirect:/project/" + projectId + "/admin-settings";
    }

    public static class NewMemberForm {
        @NotNull
        @Size(min = 3, max = 32)
        private String member;

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }
    }

    public static class BuildSettingsForm {
        @NotNull
        private String buildCommand;
        @NotNull
        private String artifactPath;
        @NotNull
        private String testCommand;
        @NotNull
        private String xmlPath;

        public String getBuildCommand() {
            return buildCommand;
        }

        public void setBuildCommand(String buildCommand) {
            this.buildCommand = buildCommand;
        }

        public String getArtifactPath() {
            return artifactPath;
        }

        public void setArtifactPath(String artifactPath) {
            this.artifactPath = artifactPath;
        }

        public String getTestCommand() {
            return testCommand;
        }

        public void setTestCommand(String testCommand) {
            this.testCommand = testCommand;
        }

        public String getXmlPath() {
            return xmlPath;
        }

        public void setXmlPath(String xmlPath) {
            this.xmlPath = xmlPath;
        }
    }
}
