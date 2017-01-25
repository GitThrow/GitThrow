package net.orekyuu.gitthrow.controller.view.admin;

import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.service.exceptions.NotAdminException;
import net.orekyuu.gitthrow.service.exceptions.ProjectNotFoundException;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice("net.orekyuu.gitthrow.controller.view.admin")
public class AdminControllerAdvice {

    @Autowired
    private UserUsecase usecase;

    @ModelAttribute
    public void addUserObject(Model model) throws ProjectNotFoundException {
        WorkbenchUserDetails userDetails = (WorkbenchUserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        if (!user.isAdmin()) {
            throw new NotAdminException();
        }
        model.addAttribute("user", user);
    }
}
