package net.orekyuu.gitthrow.controller.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Properties;

@Controller
public class EnvironmentController {

    @GetMapping("admin/environment")
    public String showAdminSetting(Model model) {
        Properties properties = System.getProperties();
        model.addAttribute("properties", properties);
        return "user/environment";
    }
}
