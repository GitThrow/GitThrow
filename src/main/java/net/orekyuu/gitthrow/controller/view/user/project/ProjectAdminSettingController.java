package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.infra.ProjectName;
import net.orekyuu.gitthrow.infra.ProjectOwnerOnly;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.service.exceptions.ProjectNotFoundException;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import net.orekyuu.gitthrow.user.util.BotUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private ProjectUsecase projectUsecase;
    @Autowired
    private UserUsecase userService;

    private static final Logger logger = LoggerFactory.getLogger(ProjectAdminSettingController.class);
    @ModelAttribute("projectMember")
    public List<User> projectMember(@PathVariable String projectId) throws ProjectNotFoundException {
        return projectUsecase.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId))
            .getMember();
    }

    @ModelAttribute("botSettingsForm")
    public BotSettingsForm botSettingsForm(@PathVariable String projectId) {
        User botUser = userService.findById(BotUserUtil.toBotUserId(projectId)).orElseThrow(NullPointerException::new);
        BotSettingsForm form = new BotSettingsForm();
        form.setName(botUser.getName());
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
        if (!Objects.equals(botUser.getName(), form.name)) {
            botUser.changeName(form.name);
            userService.save(botUser);
        }

        if (form.avatar != null && form.avatar.getSize() != 0) {
            try {
                userService.updateAvater(botUser, form.avatar.getBytes());
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
        Project project = projectUsecase.findById(projectId).get();
        if (project.getOwner().getId().equals(delete)) {
            logger.info("プロジェクト管理者を削除しようとしたのでスキップ");
        } else {
            projectUsecase.withdraw(project, userService.findById(delete).orElseThrow(() -> new UsernameNotFoundException(delete)));
        }
        return "redirect:/project/" + projectId + "/admin-settings";
    }

    @PostMapping(value = "/project/{projectId}/admin-settings/member/new")
    @ProjectOwnerOnly
    public String newMember(@ProjectName @PathVariable String projectId, @RequestParam("newMemberId") String newMemberId) {
        Project project = projectUsecase.findById(projectId).get();
        User user = userService.findById(newMemberId).orElseThrow(() -> new UsernameNotFoundException(newMemberId));
        projectUsecase.join(project, user);
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
