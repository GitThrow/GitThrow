package net.orekyuu.gitthrow.activity.port;

import net.orekyuu.gitthrow.activity.domain.model.Activity;
import net.orekyuu.gitthrow.activity.port.table.ActivityDao;
import net.orekyuu.gitthrow.activity.port.table.ActivityTable;
import net.orekyuu.gitthrow.activity.util.ActivityUtil;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ActivityRepository {

    private final ActivityDao activityDao;
    private final UserUsecase userUsecase;

    public ActivityRepository(ActivityDao activityDao, UserUsecase userUsecase) {
        this.activityDao = activityDao;
        this.userUsecase = userUsecase;
    }

    public Activity create(String title, String desc, User user, Project project) {
        ActivityTable result = activityDao.insert(new ActivityTable(null, title, desc, user.getId(), project.getId(),
            LocalDateTime.now())).getEntity();
        User u = userUsecase.findById(result.getUserId()).orElseThrow(() -> new UsernameNotFoundException(result.getUserId()));
        return ActivityUtil.fromTable(result, u);
    }

    public void deleteByProject(Project project) {
        activityDao.deleteByProjectId(project.getId());
    }

    public void delete(Activity activity) {
        activityDao.delete(new ActivityTable(activity.getId(), null, null, null, null, null));
    }

    public List<Activity> findByProject(Project project) {
        HashMap<String, User> memo = new HashMap<>();
        return activityDao.findByProject(project.getId(),
            Collectors.mapping(table -> {
                User user = memo.computeIfAbsent(table.getUserId(),
                    id -> userUsecase.findById(id).orElseThrow(() -> new UsernameNotFoundException(id)));
                return ActivityUtil.fromTable(table, user);
            }, Collectors.toList()));
    }

    public List<Activity> findByUser(User user) {
        return activityDao.findByUser(user.getId(),
            Collectors.mapping(table -> ActivityUtil.fromTable(table, user), Collectors.toList()));
    }

    public Optional<Activity> findById(int id) {
        return activityDao.findById(id).map(table -> {
            User user = userUsecase.findById(table.getUserId()).orElseThrow(() -> new UsernameNotFoundException(table.getUserId()));
            return ActivityUtil.fromTable(table, user);
        });
    }

}
