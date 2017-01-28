package net.orekyuu.gitthrow.activity.usecase;

import net.orekyuu.gitthrow.activity.domain.model.Activity;
import net.orekyuu.gitthrow.util.UsecaseTestBase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityUsecaseTest extends UsecaseTestBase {

    @Autowired
    private ActivityUsecase activityUsecase;

    @Test
    public void createActivityTest() {
        activityUsecase.createInitRepositoryActivity(project, user1);
        Assertions.assertThat(activityUsecase.findByProject(project)).hasSize(2);
    }

    @Test
    public void findByProject() {
        activityUsecase.createInitRepositoryActivity(project, user1);
        activityUsecase.createInitRepositoryActivity(project, user2);
        Assertions.assertThat(activityUsecase.findByProject(project)).hasSize(3);
    }

    @Test
    public void findByUser() {
        activityUsecase.createInitRepositoryActivity(project, user1);
        activityUsecase.createInitRepositoryActivity(project, user2);
        Assertions.assertThat(activityUsecase.findByUser(user1)).hasSize(2);
    }

    @Test
    public void findById() {
        Activity activity = activityUsecase.createInitRepositoryActivity(project, user1);
        Assertions.assertThat(activityUsecase.findById(activity.getId()))
            .isPresent()
            .map(Activity::getId)
            .hasValue(activity.getId());
    }

    @Test
    public void deleteByProject() {
        activityUsecase.createInitRepositoryActivity(project, user1);
        activityUsecase.createInitRepositoryActivity(project, user2);
        activityUsecase.deleteByProject(project);
        Assertions.assertThat(activityUsecase.findByProject(project)).isEmpty();
    }

    @Test
    public void delete() {
        activityUsecase.createInitRepositoryActivity(project, user1);
        Activity activity = activityUsecase.createInitRepositoryActivity(project, user2);
        activityUsecase.delete(activity);
        Assertions.assertThat(activityUsecase.findByProject(project)).hasSize(2);
    }
}
