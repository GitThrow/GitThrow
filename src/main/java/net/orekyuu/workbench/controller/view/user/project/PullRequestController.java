package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.rest.model.PullRequestModel;
import net.orekyuu.workbench.entity.OpenPullRequest;
import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.PullRequestService;
import net.orekyuu.workbench.service.RemoteRepositoryService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.service.exceptions.PullRequestNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PullRequestController {

    @Autowired
    private PullRequestService pullRequestService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RemoteRepositoryService remoteRepositoryService;

    @GetMapping("/project/{projectId}/pull-request")
    @ProjectMemberOnly
    public String show(@ProjectName @PathVariable String projectId, Model model) {
        model.addAttribute("pullRequestList", pullRequestService.findByProject(projectId));
        return "user/project/pull-request-list";
    }

    @ModelAttribute
    public NewPullRequestForm newPullRequestForm() {
        return new NewPullRequestForm();
    }

    @GetMapping("/project/{projectId}/pull-request/create")
    @ProjectMemberOnly
    public String showNewPullRequest(@ProjectName @PathVariable String projectId, Model model) throws ProjectNotFoundException {
        List<User> projectMember = projectService.findProjectMember(projectId);
        model.addAttribute("member", projectMember);

        List<String> branch = remoteRepositoryService.findBranch(projectId).stream()
            .map(str -> str.substring("refs/heads/".length()))
            .collect(Collectors.toList());
        model.addAttribute("branch", branch);
        return "user/project/new-pull-request";
    }

    @PostMapping("/project/{projectId}/pull-request/create")
    @ProjectMemberOnly
    public String createPullRequest(@ProjectName @PathVariable String projectId,
                                    @Valid NewPullRequestForm form, BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(projectId, form.reviewer)) {
            result.addError(new FieldError("newPullRequestForm", "reviewer", "プロジェクトメンバーでありません"));
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newPullRequestForm", result);
            redirectAttributes.addFlashAttribute("newPullRequestForm", form);
            return "redirect:/project/" + projectId + "/pull-request/create";
        }

        OpenPullRequest pullRequest = new OpenPullRequest();
        pullRequest.project = projectId;
        pullRequest.title = form.title;
        pullRequest.description = form.desc;
        pullRequest.reviewer = form.reviewer;
        pullRequest.baseBranch = form.baseBranch;
        pullRequest.targetBranch = form.targetBranch;
        pullRequest.proponent = principal.getUser().id;
        pullRequestService.create(pullRequest);

        return "redirect:/project/" + projectId + "/pull-request";
    }

    @GetMapping("/project/{projectId}/pull-request/{num}")
    @ProjectMemberOnly
    public String showDetail(@ProjectName @PathVariable String projectId, @PathVariable int num, Model model) {
        PullRequestModel pullRequestModel = pullRequestService.findByProjectAndNum(projectId, num)
            .orElseThrow(() -> new PullRequestNotFoundException(projectId));
        model.addAttribute("pullRequest", pullRequestModel);
        return "user/project/pull-request-detail";
    }

    static class NewPullRequestForm {
        @NotNull
        @Size(min = 1, max = 128)
        private String title;
        @NotNull
        private String desc;
        @NotNull
        @Size(min = 1, max = 256)
        private String baseBranch;
        @NotNull
        @Size(min = 1, max = 256)
        private String targetBranch;
        @NotNull
        @Size(min = 1, max = 32)
        private String reviewer;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getBaseBranch() {
            return baseBranch;
        }

        public void setBaseBranch(String baseBranch) {
            this.baseBranch = baseBranch;
        }

        public String getTargetBranch() {
            return targetBranch;
        }

        public void setTargetBranch(String targetBranch) {
            this.targetBranch = targetBranch;
        }

        public String getReviewer() {
            return reviewer;
        }

        public void setReviewer(String reviewer) {
            this.reviewer = reviewer;
        }
    }
}
