package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.BuildSettings;
import net.orekyuu.workbench.entity.TestSettings;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.ProjectSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
public class ProjectAdminSettingController {

    @Autowired
    private ProjectSettingService projectSettingService;

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
