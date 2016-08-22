package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.entity.*;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.*;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class CreateTicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketTypeService typeService;
    @Autowired
    private TicketStatusService statusService;
    @Autowired
    private TicketPriorityService priorityService;
    @Autowired
    private ProjectService projectService;

    @ProjectMemberOnly
    @GetMapping("/project/{projectId}/ticket/create")
    public String show(@ProjectName @PathVariable String projectId, Model model) throws ProjectNotFoundException {
        setupModel(model, projectId);

        return "user/project/new-ticket";
    }

    @ProjectMemberOnly
    @PostMapping("/project/{projectId}/ticket/create")
    public String createTicket(@ProjectName @PathVariable String projectId, Model model,
                               @Valid CreateTicketForm form, BindingResult result,
                               @AuthenticationPrincipal WorkbenchUserDetails principal) throws ProjectNotFoundException {
        setupModel(model, projectId);

        if (result.hasErrors()) {
            return "user/project/new-ticket";
        }

        OpenTicket ticket = new OpenTicket();
        ticket.project = projectId;
        ticket.proponent = principal.getUser().id;
        ticket.title = form.title;
        ticket.description = form.desc == null ? "" : form.desc;
        ticket.status = form.status; //違うプロジェクトのものが指定されるかも
        ticket.priority = form.priority;
        ticket.type = form.type;
        ticket.assignee = form.assignee; //プロジェクトに参加していないユーザーが割り当てられるかも
        Date limit = form.limit;
        if (limit != null) {
            ticket.limit = limit.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        ticketService.createTicket(ticket);

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

    private void setupModel(Model model, String projectId) throws ProjectNotFoundException {
        List<TicketType> typeList = typeService.findByProject(projectId);
        List<TicketStatus> statusList = statusService.findByProject(projectId);
        List<TicketPriority> priorityList = priorityService.findByProject(projectId);
        List<User> member = projectService.findProjectMember(projectId);

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
