package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/user")
public class UserRestController {

    @Autowired
    private UserUsecase usecase;

    @GetMapping("all")
    public List<User> allUser() {
        return usecase.findAll(false);
    }
}
