package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.entity.UserAvatar;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.infra.ProjectOwnerOnly;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.UserService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.util.BotUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

@Controller
public class ProjectAdminSettingController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ProjectAdminSettingController.class);
    @ModelAttribute("projectMember")
    public List<User> projectMember(@PathVariable String projectId) throws ProjectNotFoundException {
        List<User> projectMember = projectService.findProjectMember(projectId);
        return projectMember;
    }

    @ModelAttribute("botSettingsForm")
    public BotSettingsForm botSettingsForm(@PathVariable String projectId) {
        User botUser = userService.findById(BotUserUtil.toBotUserId(projectId)).orElseThrow(NullPointerException::new);
        BotSettingsForm form = new BotSettingsForm();
        form.setName(botUser.name);
        return form;
    }

    @ModelAttribute("botUserId")
    public String botUserId(@PathVariable String projectId) {
        return BotUserUtil.toBotUserId(projectId);
    }

    @ProjectOwnerOnly
    @GetMapping("/project/{projectId}/admin-settings")
    public String show(@ProjectName @PathVariable String projectId, Model model) {
        return "user/project/admin-setting";
    }

    @ProjectOwnerOnly
    @PostMapping(value = "/project/{projectId}/admin-settings/bot")
    public String updateBotSettings(@ProjectName @PathVariable String projectId,
                                    @Valid BotSettingsForm form, BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.botSettingsForm", result);
            redirectAttributes.addFlashAttribute("botSettingsForm", form);
            return "redirect:/project/" + projectId + "/admin-settings";
        }

        User botUser = userService.findById(BotUserUtil.toBotUserId(projectId)).orElseThrow(NullPointerException::new);
        if (!Objects.equals(botUser.name, form.name)) {
            botUser.name = form.name;
            userService.update(botUser);
        }

        if (form.avatar != null && form.avatar.getSize() != 0) {
            try {
                byte[] bytes = form.avatar.getBytes();
                UserAvatar userAvatar = new UserAvatar();
                userAvatar.id = botUser.id;
                userAvatar.avatar = bytes;
                userService.updateIcon(userAvatar);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return "redirect:/project/" + projectId + "/admin-settings";
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

    public static class BotSettingsForm {
        @Size(min = 3, max = 16)
        private String name;

        private MultipartFile avatar;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MultipartFile getAvatar() {
            return avatar;
        }

        public void setAvatar(MultipartFile avatar) {
            this.avatar = avatar;
        }
    }
}
