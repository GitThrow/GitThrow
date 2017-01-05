package net.orekyuu.workbench.controller.view.user;

import net.orekyuu.workbench.infra.AdminOnly;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Controller
public class AdminSettingController {

    @Autowired
    private UserUsecase userUsecase;

    @GetMapping("admin/user-setting")
    @AdminOnly
    public String showAdminSetting(Model model) {
        model.addAttribute("users", userUsecase.findAll(false));
        return "user/user-management";
    }

    @PostMapping("admin/user-setting/register")
    @AdminOnly
    public String createNewUser(@Valid NewUserForm form, BindingResult result,
                                RedirectAttributes redirectAttributes) {
        userUsecase.findById(form.id).ifPresent(u -> {
            result.addError(new FieldError("newUserForm", "id", "使用されているIDです"));
        });

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newUserForm", result);
            redirectAttributes.addFlashAttribute("newUserForm", form);
            return "redirect:/admin/user-setting";
        }

        try {
            userUsecase.create(form.id, form.name, form.password);
        } catch (UserExistsException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/admin/user-setting";
    }

    @ModelAttribute
    public NewUserForm newUserForm() {
        return new NewUserForm();
    }


    class NewUserForm {
        @Size(min = 3, max = 32)
        private String id;
        @Size(min = 0, max = 16)
        private String name;
        @Size(min = 0, max = 16)
        private String password;

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
