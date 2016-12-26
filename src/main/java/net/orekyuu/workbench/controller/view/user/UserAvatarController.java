package net.orekyuu.workbench.controller.view.user;

import net.orekyuu.workbench.user.usecase.UserUsecase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserAvatarController {

    @Autowired
    private UserUsecase usecase;

    private static final Logger logger = LoggerFactory.getLogger(UserAvatarController.class);

    @GetMapping("/user/icon/{userId}")
    public void showUserAvatar(@PathVariable("userId") String userId, HttpServletResponse response) {
        try {
            byte[] iconData = usecase.findAvater(userId);
            response.setHeader("Content-Type", "image/png");
            response.getOutputStream().write(iconData);
        } catch (Exception e) {
            logger.warn("Avater not found. userId=" + userId + " exception: " + e.getClass().getName());
            response.setStatus(404);
        }

    }
}
