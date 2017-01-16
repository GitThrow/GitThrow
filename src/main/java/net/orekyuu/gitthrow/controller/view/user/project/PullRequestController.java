package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.build.usecase.WorkbenchConfigUsecase;
import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.controller.exception.ResourceNotFoundException;
import net.orekyuu.gitthrow.git.domain.RemoteRepository;
import net.orekyuu.gitthrow.git.domain.RemoteRepositoryFactory;
import net.orekyuu.gitthrow.infra.ProjectMemberOnly;
import net.orekyuu.gitthrow.infra.ProjectName;
import net.orekyuu.gitthrow.job.WorkbenchConfig;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestUsecase;
import net.orekyuu.gitthrow.service.exceptions.ProjectNotFoundException;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private PullRequestUsecase pullrequestUsecase;
    @Autowired
    private RemoteRepositoryFactory repositoryFactory;
    @Autowired
    private UserUsecase userUsecase;
    @Autowired
    private WorkbenchConfigUsecase configUsecase;

    @GetMapping("/project/{projectId}/pull-request")
    @ProjectMemberOnly
    public String show(@ProjectName @PathVariable String projectId) {
        return "user/project/pull-request-list";
    }

    @ModelAttribute
    public NewPullRequestForm newPullRequestForm() {
        return new NewPullRequestForm();
    }

    @GetMapping("/project/{projectId}/pull-request/create")
    @ProjectMemberOnly
    public String showNewPullRequest(@ProjectName @PathVariable String projectId, Model model, Project project) throws ProjectNotFoundException {
        model.addAttribute("member", project.getMember());

        RemoteRepository repository = repositoryFactory.create(projectId);
        List<String> branch = repository.findBranch().stream()
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
                                    @AuthenticationPrincipal WorkbenchUserDetails principal,
                                    Project project) {

        User reviewer = userUsecase.findById(form.reviewer).orElseThrow(() -> new UsernameNotFoundException(form.reviewer));
        if (!project.getMember().contains(reviewer)) {
            result.addError(new FieldError("newPullRequestForm", "reviewer", "プロジェクトメンバーでありません"));
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newPullRequestForm", result);
            redirectAttributes.addFlashAttribute("newPullRequestForm", form);
            return "redirect:/project/" + projectId + "/pull-request/create";
        }

        PullRequest request = pullrequestUsecase.create(project, form.title, form.desc, reviewer, principal.getUser(), form.baseBranch, form.targetBranch);

        return "redirect:/project/" + projectId + "/pull-request/" + request.getPullrequestNum();
    }

    @GetMapping("/project/{projectId}/pull-request/{num}")
    @ProjectMemberOnly
    public String showDetail(@ProjectName @PathVariable String projectId, @PathVariable int num, Model model, Project project) {
        PullRequest pullRequest = pullrequestUsecase.findById(project, num).orElseThrow(ResourceNotFoundException::new);
        WorkbenchConfig config = configUsecase.find(project, pullRequest.getTarget()).orElse(null);
        model.addAttribute("pullRequest", pullRequest);
        model.addAttribute("config", config);
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
