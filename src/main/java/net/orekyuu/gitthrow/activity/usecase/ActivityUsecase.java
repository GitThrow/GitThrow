package net.orekyuu.gitthrow.activity.usecase;

import net.orekyuu.gitthrow.activity.domain.model.Activity;
import net.orekyuu.gitthrow.activity.port.ActivityRepository;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.ticket.domain.model.Ticket;
import net.orekyuu.gitthrow.user.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ActivityUsecase {

    private final ActivityRepository activityRepository;
    private static final Logger logger = LoggerFactory.getLogger(ActivityUsecase.class);

    public ActivityUsecase(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * リポジトリを作成したときのアクティビティを作る
     * @param project 開始したプロジェクト
     * @param user プロジェクトのオーナー
     * @return 作成したアクティビティ
     */
    public Activity createInitRepositoryActivity(Project project, User user) {
        return create("リポジトリを作成しました", "", user, project);
    }

    /**
     * 新しいチケットを作ったときのActivity
     * @param project プロジェクト
     * @param ticket 新しく作ったチケット
     * @return 作成したアクティビティ
     */
    public Activity createNewTicketActivity(Project project, Ticket ticket) {
        return create("新しいチケット",
            String.format("[#%d %s](/project/%s/ticket/%d)",
                ticket.getTicketNum(), ticket.getTitle(), project.getId(), ticket.getTicketNum()),
            ticket.getProponent(), project);
    }

    /**
     * 新しいプルリクエストを作ったときのActivity
     * @param project プロジェクト
     * @param pullRequest 新しく作ったプルリクエスト
     * @return 作成したアクティビティ
     */
    public Activity createNewPullRequestActivity(Project project, PullRequest pullRequest) {
        return create("新しいプルリクエスト",
            String.format("[#%d %s](#)", pullRequest.getPullrequestNum(), pullRequest.getTitle()),
            pullRequest.getProponent(), project);
    }

    /**
     * プルリクエストをマージしたときのActivity
     * @param project プロジェクト
     * @param mergedPullRequest マージしたプルリクエスト
     * @return 作成したアクティビティ
     */
    public Activity createMergePullRequestActivity(Project project, PullRequest mergedPullRequest) {
        return create("プルリクエストをマージ",
            String.format("[#%d %s](#)", mergedPullRequest.getPullrequestNum(), mergedPullRequest.getTitle()),
            mergedPullRequest.getProponent(), project);
    }

    @Transactional(readOnly = false)
    private Activity create(String title, String desc, User user, Project project) {
        logger.info("[Activity] create: title=%s, desc=%s, user=%s, project=%s", title, desc, user.getId(), project.getId());
        return activityRepository.create(title, desc, user, project);
    }

    @Transactional(readOnly = false)
    public void deleteByProject(Project project) {
        activityRepository.deleteByProject(project);
    }

    @Transactional(readOnly = false)
    public void delete(Activity activity) {
        activityRepository.delete(activity);
    }

    public List<Activity> findByProject(Project project) {
        return activityRepository.findByProject(project).stream()
            .sorted(Comparator.comparing(Activity::getCreatedAt).reversed())
            .collect(Collectors.toList());
    }

    public List<Activity> findByUser(User user) {
        return activityRepository.findByUser(user).stream()
            .sorted(Comparator.comparing(Activity::getCreatedAt).reversed())
            .collect(Collectors.toList());
    }

    public Optional<Activity> findById(int id) {
        return activityRepository.findById(id);
    }
}
