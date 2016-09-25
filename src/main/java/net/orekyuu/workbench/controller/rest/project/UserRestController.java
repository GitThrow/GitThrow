package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.controller.rest.model.UserModel;
import net.orekyuu.workbench.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("all")
    public List<UserModel> allUser() {
        return userService.findAll(false).stream()
            .map(UserModel::new)
            .collect(Collectors.toList());
    }
}
