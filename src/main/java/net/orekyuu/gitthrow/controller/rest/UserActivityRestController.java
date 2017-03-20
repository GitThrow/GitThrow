package net.orekyuu.gitthrow.controller.rest;

import net.orekyuu.gitthrow.activity.domain.model.Activity;
import net.orekyuu.gitthrow.activity.usecase.ActivityUsecase;
import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/user/activity")
public class UserActivityRestController {

    @Autowired
    private ActivityUsecase usecase;

    @GetMapping
    public List<Activity> show(@AuthenticationPrincipal WorkbenchUserDetails userDetails) {
        return usecase.findByUser(userDetails.getUser()).stream()
            .sorted(Comparator.comparing(Activity::getCreatedAt))
            .limit(10)
            .collect(Collectors.toList());
    }
}
