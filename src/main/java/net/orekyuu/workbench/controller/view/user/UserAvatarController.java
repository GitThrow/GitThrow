package net.orekyuu.workbench.controller.view.user;

import net.orekyuu.workbench.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserAvatarController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/icon/{userId}")
    public void showUserAvatar(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {
        byte[] iconData = userService.findAvatar(userId).orElseThrow(() -> new UsernameNotFoundException(userId));

        response.setHeader("Content-Type", "image/png");
        response.getOutputStream().write(iconData);

    }
}
