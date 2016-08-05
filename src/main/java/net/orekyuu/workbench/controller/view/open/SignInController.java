package net.orekyuu.workbench.controller.view.open;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInController {

    @GetMapping(value = "/login")
    public String showIndex() {
        return "open/index";
    }
}
