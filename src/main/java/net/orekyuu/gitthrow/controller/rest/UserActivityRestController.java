package net.orekyuu.gitthrow.controller.rest;

import net.orekyuu.gitthrow.activity.domain.model.Activity;
import net.orekyuu.gitthrow.activity.usecase.ActivityUsecase;
import net.orekyuu.gitthrow.controller.exception.ResourceNotFoundException;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/user/{user}/activity")
public class UserActivityRestController {
    @Autowired
    private UserUsecase userUsecase;
    @Autowired
    private ActivityUsecase activityUsecase;

    @GetMapping
    public List<Activity> userActivity(@PathVariable("user") String userId) {
        User user = userUsecase.findById(userId).orElseThrow(ResourceNotFoundException::new);
        return activityUsecase.findByUser(user);
    }
}
