package net.orekyuu.workbench.pullrequest.usecase;

import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.project.usecase.ProjectUsecase;
import net.orekyuu.workbench.pullrequest.domain.model.PullRequest;
import net.orekyuu.workbench.pullrequest.domain.model.PullRequestState;
import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.usecase.UserUsecase;
import net.orekyuu.workbench.util.ProjectTestUtil;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PullRequestUsecaseTest {

    @Autowired
    private ProjectUsecase projectUsecase;
    @Autowired
    private PullRequestUsecase pullRequestUsecase;
    @Autowired
    private UserUsecase userUsecase;

    @Autowired
    private ProjectTestUtil util;

    private User user1;
    private User user2;
    private Project project;

    public static Condition<PullRequest> testFields(long prNum, String projectId, String title, String desc, PullRequestState state, User reviewer, User proponent, String base, String target) {
        return new Condition<>(pr ->
            pr.getPullrequestNum() == prNum &&
            pr.getProjectId().equals(projectId) &&
            pr.getState() == state &&
            pr.getTitle().equals(title) &&
            pr.getDescription().equals(desc) &&
            pr.getReviewer().equals(reviewer) &&
            pr.getProponent().equals(proponent) &&
            pr.getBase().equals(base) &&
            pr.getTarget().equals(target),
            "すべてのフィールドが等しいか");
    }

    @Before
    public void setUp() throws Exception {
        user1 = userUsecase.create("user1", "user1", "password");
        user2 = userUsecase.create("user2", "user2", "password");

        project = projectUsecase.createProject("project1", "project1", user1);
        projectUsecase.join(project, user2);
    }

    @After
    public void tearDown() throws Exception {
        util.deleteGitRepositoryAndWorkspaceDir();
    }

    @Test
    public void create() throws Exception {
        PullRequest pr = pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");
        Assertions.assertThat(pr)
            .is(testFields(1, "project1", "test", "desc", PullRequestState.OPEN, user1, user2, "base_hash", "target_hash"));

        PullRequest pr2 = pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");
        Assertions.assertThat(pr2)
            .is(testFields(2, "project1", "test", "desc", PullRequestState.OPEN, user1, user2, "base_hash", "target_hash"));

        Assertions.assertThat(pullRequestUsecase.findByProject(project))
            .hasSize(2);
    }

    @Test
    public void update() throws Exception {
        PullRequest pr = pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");
        pr.setTitle("hoge");
        pr.setDescription("fuga");
        pr.setReviewer(user2);

        PullRequest update = pullRequestUsecase.update(pr);
        Assertions.assertThat(pr)
            .is(testFields(1, "project1", "hoge", "fuga", PullRequestState.OPEN, user2, user2, "base_hash", "target_hash"));

        PullRequest request = pullRequestUsecase.findById(project, 1).get();
        Assertions.assertThat(request)
            .is(testFields(1, "project1", "hoge", "fuga", PullRequestState.OPEN, user2, user2, "base_hash", "target_hash"));
    }

    @Test
    public void merge() throws Exception {
        PullRequest pr = pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");
        Assertions.assertThat(pr)
            .is(testFields(1, "project1", "test", "desc", PullRequestState.OPEN, user1, user2, "base_hash", "target_hash"));


        PullRequest merge = pullRequestUsecase.merge(pr, "base_commit", "target_commit");
        Assertions.assertThat(merge)
            .is(testFields(1, "project1", "test", "desc", PullRequestState.MERGED, user1, user2, "base_commit", "target_commit"));

        PullRequest request = pullRequestUsecase.findById(project, 1).get();
        Assertions.assertThat(request)
            .is(testFields(1, "project1", "test", "desc", PullRequestState.MERGED, user1, user2, "base_commit", "target_commit"));
    }

    @Test
    public void deleteByProject() throws Exception {
        pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");
        pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");

        Assertions.assertThat(pullRequestUsecase.findByProject(project)).hasSize(2);

        pullRequestUsecase.deleteByProject(project);

        Assertions.assertThat(pullRequestUsecase.findByProject(project)).hasSize(0);
    }

    @Test
    public void findByProject() throws Exception {
        pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");
        pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");

        Assertions.assertThat(pullRequestUsecase.findByProject(project)).hasSize(2);
    }

    @Test
    public void findById() throws Exception {
        Assertions.assertThat(pullRequestUsecase.findById(project, 1)).isNotPresent();
        pullRequestUsecase.create(project, "test", "desc", user1, user2, "base_hash", "target_hash");
        Assertions.assertThat(pullRequestUsecase.findById(project, 1)).isPresent();
    }

}
