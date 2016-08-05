package net.orekyuu.workbench.controller.view.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserIndexPageController {

    @GetMapping("/")
    public String showIndexPage() {
        return "user/index";
    }
}
