package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.BuildSettings;
import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.entity.TestSettings;
import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.ProjectSettingService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Controller
public class ProjectAdminSettingController {

    @Autowired
    private ProjectSettingService projectSettingService;
    @Autowired
    private ProjectService projectService;

    private static final Logger logger = LoggerFactory.getLogger(ProjectAdminSettingController.class);
    @ModelAttribute("projectMember")
    public List<User> projectMember(@PathVariable String projectId) throws ProjectNotFoundException {
        List<User> projectMember = projectService.findProjectMember(projectId);
        return projectMember;
    }

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/admin-settings")
    public String show(@ProjectName @PathVariable String projectId) {
        return "user/project/admin-setting";
    }

    @ProjectMemberOnly
    @PostMapping("/project/{projectId}/admin-settings/build-settings")
    public String updateBuildSettings(@ProjectName @PathVariable String projectId,
                                      @Valid BuildSettingsForm form, BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            //↓なんかこれまずい気がする・・・ちゃんとした方法が別であるのかな？
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.buildSettingsForm", result);
            redirectAttributes.addFlashAttribute("buildSettingsForm", form);
            return "redirect:/project/" + projectId + "/admin-settings#build-settings";
        }

        BuildSettings buildSettings = new BuildSettings();
        buildSettings.projectId = projectId;
        buildSettings.buildCommand = form.getBuildCommand();
        buildSettings.artifactPath = form.getArtifactPath();
        projectSettingService.updateBuildSettings(buildSettings);

        TestSettings testSettings = new TestSettings();
        testSettings.projectId = projectId;
        testSettings.testCommand = form.getTestCommand();
        testSettings.xmlPath = form.getXmlPath();
        projectSettingService.updateTestSettings(testSettings);

        return "redirect:/project/" + projectId + "/admin-settings";
    }

    @ModelAttribute
    public BuildSettingsForm buildSettingsForm(@PathVariable String projectId) {
        BuildSettings buildSettings = projectSettingService.findBuildSettings(projectId).orElseThrow(IllegalArgumentException::new);
        TestSettings testSettings = projectSettingService.findTestSettings(projectId).orElseThrow(IllegalArgumentException::new);
        BuildSettingsForm form = new BuildSettingsForm();
        form.setBuildCommand(buildSettings.buildCommand);
        form.setArtifactPath(buildSettings.artifactPath);
        form.setTestCommand(testSettings.testCommand);
        form.setXmlPath(testSettings.xmlPath);
        return form;
    }

    //メンバーの削除
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
