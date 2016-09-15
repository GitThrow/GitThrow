package net.orekyuu.workbench.service;

import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.entity.OpenPullRequest;
import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.util.TestRepositoryUtil;
import org.assertj.core.api.Assertions;
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
public class PullRequestServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private PullRequestService pullRequestService;

    @Autowired
    private TestRepositoryUtil testRepositoryUtil;

    private User user1;
    private User user2;

    @Before
    public void before() throws UserExistsException, ProjectExistsException {
        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();

        userService.createUser("user1", "user1", "pw");
        userService.createUser("user2", "user2", "pw");

        user1 = userService.findById("user1").get();
        user2 = userService.findById("user2").get();

        projectService.createProject("project1", "project1", user1);
    }

    private void createPullRequest(String title) {
        OpenPullRequest pr = new OpenPullRequest();
        pr.title = title;
        pr.description = "";
        pr.project = "project1";
        pr.reviewer = user1.id;
        pr.proponent = user1.id;
        pr.baseBranch = "base";
        pr.targetBranch = "target";
        pullRequestService.create(pr);
    }

    @Test
    public void testCreate() {
        createPullRequest("test1");
        createPullRequest("test2");
    }

    @Test
    public void testCreateException() {
        Assertions.assertThatThrownBy(() -> {
            OpenPullRequest pr = new OpenPullRequest();
            pr.title = "test1";
            pr.description = "";
            pr.project = "project1";
            pr.reviewer = user2.id;//プロジェクトメンバーではない
            pr.proponent = user1.id;
            pr.baseBranch = "base";
            pr.targetBranch = "target";
            pullRequestService.create(pr);
        }).isInstanceOf(NotMemberException.class);

        Assertions.assertThatThrownBy(() -> {
            OpenPullRequest pr = new OpenPullRequest();
            pr.title = "test1";
            pr.description = "";
            pr.project = "project1";
            pr.reviewer = user1.id;
            pr.proponent = user2.id;//プロジェクトメンバーではない
            pr.baseBranch = "base";
            pr.targetBranch = "target";
            pullRequestService.create(pr);
        }).isInstanceOf(NotMemberException.class);
    }

    @Test
    public void testDelete() {
        createPullRequest("test1");
        createPullRequest("test2");

        Assertions.assertThat(pullRequestService.findByProject("project1")).hasSize(2);

        pullRequestService.deleteByProject("project1");

        Assertions.assertThat(pullRequestService.findByProject("project1")).hasSize(0);
    }

    @Test
    public void testFindByProject() {
        createPullRequest("test1");
        createPullRequest("test2");
        Assertions.assertThat(pullRequestService.findByProject("project1")).hasSize(2);
    }
}
