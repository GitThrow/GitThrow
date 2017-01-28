package net.orekyuu.gitthrow.controller.rest.project;

import net.orekyuu.gitthrow.activity.domain.model.Activity;
import net.orekyuu.gitthrow.activity.usecase.ActivityUsecase;
import net.orekyuu.gitthrow.project.domain.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/{project}/activity")
public class ActivityRestController {

    @Autowired
    private ActivityUsecase activityUsecase;

    @GetMapping
    public List<Activity> findByProject(Project project) {
        return activityUsecase.findByProject(project);
    }
}
