package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.infra.ProjectMemberOnly;
import net.orekyuu.gitthrow.infra.ProjectName;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.service.exceptions.ProjectNotFoundException;
import net.orekyuu.gitthrow.ticket.domain.model.TicketPriority;
import net.orekyuu.gitthrow.ticket.domain.model.TicketStatus;
import net.orekyuu.gitthrow.ticket.domain.model.TicketType;
import net.orekyuu.gitthrow.ticket.usecase.TicketUsecase;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class CreateTicketController {

    @Autowired
    private TicketUsecase ticketUsecase;
    @Autowired
    private ProjectUsecase projectService;
    @Autowired
    private UserUsecase userUsecase;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/ticket/create")
    public String show(@ProjectName @PathVariable String projectId, Model model) throws ProjectNotFoundException {
        Project project = projectService.findById(projectId).get();
        setupModel(model, project);

        return "user/project/new-ticket";
    }

    @ProjectMemberOnly
    @PostMapping("/project/{projectId}/ticket/create")
    public String createTicket(@ProjectName @PathVariable String projectId, Model model,
                               @Valid CreateTicketForm form, BindingResult result,
                               @AuthenticationPrincipal WorkbenchUserDetails principal) throws ProjectNotFoundException {
        Project project = projectService.findById(projectId).get();
        setupModel(model, project);

        if (result.hasErrors()) {
            return "user/project/new-ticket";
        }


        LocalDateTime time = null;
        Date limit = form.limit;
        if (limit != null) {
            time = limit.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        ticketUsecase.create(
            project,
            form.title,
            form.desc,
            userUsecase.findById(form.getAssignee()).orElse(null),
            principal.getUser(),
            time,
            new TicketType(form.getType(), ""),
            new TicketStatus(form.getStatus(), ""),
            new TicketPriority(form.getPriority(), ""));


        return "redirect:/project/" + projectId + "/ticket";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }

    @ModelAttribute
    public CreateTicketForm createTicketForm() {
        return new CreateTicketForm();
    }

    private void setupModel(Model model, Project project) throws ProjectNotFoundException {
        List<TicketType> typeList = ticketUsecase.findTypeByProject(project);
        List<TicketStatus> statusList = ticketUsecase.findStatusByProject(project);
        List<TicketPriority> priorityList = ticketUsecase.findPriorityByProject(project);
        List<User> member = project.getMember();

        model.addAttribute("typeList", typeList);
        model.addAttribute("statusList", statusList);
        model.addAttribute("priorityList", priorityList);
        model.addAttribute("member", member);
    }

    public static class CreateTicketForm {
        @NotBlank
        @Size(max = 128)
        private String title;
        private String desc;
        @NotNull
        private int type;
        @NotNull
        private int priority;
        @NotNull
        private int status;
        @Size(max = 32)
        private String assignee;
        private Date limit;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAssignee() {
            return assignee;
        }

        public void setAssignee(String assignee) {
            this.assignee = assignee;
        }

        public Date getLimit() {
            return limit;
        }

        public void setLimit(Date limit) {
            this.limit = limit;
        }
    }
}
