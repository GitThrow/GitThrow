package net.orekyuu.workbench.controller.view.user;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.project.usecase.ProjectUsecase;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.user.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Controller
public class NewProjectController {

    @Autowired
    private ProjectUsecase projectUsecase;

    @GetMapping("/new-project")
    public String show() {
        return "user/new-project";
    }

    @PostMapping("/new-project")
    public String post(@Valid CreateProjectForm form, BindingResult result,
                       @AuthenticationPrincipal WorkbenchUserDetails principal,
                       Model model) {

        if (result.hasErrors()) {
            return "user/new-project";
        }

        User user = principal.getUser();
        try {
            projectUsecase.createProject(form.id, form.name, user);
        } catch (ProjectExistsException e) {
            result.addError(new FieldError("id", "id", "すでに存在しているプロジェクトです"));
            return "user/new-project";
        }
        return "redirect:/project/" + form.id;
    }

    @ModelAttribute
    public CreateProjectForm createProjectForm() {
        return new CreateProjectForm();
    }

    public static class CreateProjectForm {
        @Pattern(regexp = "^[a-zA-Z0-9]{3,16}$")
        private String id;
        @Size(min = 3, max = 16)
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
