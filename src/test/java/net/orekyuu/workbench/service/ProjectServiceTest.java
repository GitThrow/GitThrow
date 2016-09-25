package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.exceptions.NotProjectMemberException;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.util.BotUserUtil;
import net.orekyuu.workbench.util.TestRepositoryUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    private User user1;
    private User user2;

    @Autowired
    private TestRepositoryUtil testRepositoryUtil;

    @Before
    public void before() throws UserExistsException, IOException {
        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();

        userService.createUser("user1", "user1", "pw");
        userService.createUser("user2", "user2", "pw");

        user1 = userService.findById("user1").get();
        user2 = userService.findById("user2").get();

    }

    @Test
    public void testCreateProject() throws ProjectExistsException {
        projectService.createProject("project1", "project1", user1);
    }

    @Test(expected = ProjectExistsException.class)
    public void testCreateProjectException() throws ProjectExistsException {
        try {
            projectService.createProject("project1", "project1", user1);
        } catch (ProjectExistsException e) {
            Assertions.fail("ここで存在しているのはおかしい");
        }
        projectService.createProject("project1", "project1", user1);
    }

    @Test
    public void testJoin() throws ProjectExistsException, ProjectNotFoundException {
        projectService.createProject("project1", "project1", user1);
        projectService.createProject("project2", "project2", user1);

        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(1);
        projectService.joinToProject("project1", user2.id);
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(2);

        projectService.joinToProject("project1", user2.id);
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(2);

        //他のプロジェクトのbotユーザーを追加できない
        Assertions.assertThatThrownBy(() -> projectService.withdrawProject("project2", BotUserUtil.toBotUserId("project1")))
            .isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    public void testWithdraw() throws ProjectExistsException, ProjectNotFoundException {
        projectService.createProject("project1", "project1", user1);
        projectService.joinToProject("project1", user2.id);
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(2);

        projectService.withdrawProject("project1", user2.id);
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(1);

        projectService.withdrawProject("project1", user2.id);
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(1);

        //botユーザーは脱退できない
        Assertions.assertThatThrownBy(() -> projectService.withdrawProject("project1", BotUserUtil.toBotUserId("project1")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithdrawException() throws ProjectExistsException, ProjectNotFoundException {
        projectService.createProject("project1", "project1", user1);
        projectService.withdrawProject("project1", user1.id);
    }

    @Test
    public void testFindMember() throws ProjectExistsException, ProjectNotFoundException {
        projectService.createProject("project1", "project1", user1);
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(1); //botユーザーはカウントされない
        projectService.joinToProject("project1", user2.id);
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(2);
    }

    @Test
    public void testUpdateProjectName() throws Exception {
        projectService.createProject("project1", "project1", user1);
        Assertions.assertThat(projectService.findById("project1").map(p -> p.name)).isNotEmpty().containsSame("project1");

        projectService.updateProjectName("project1", "hoge");
        Assertions.assertThat(projectService.findById("project1").map(p -> p.name)).isNotEmpty().containsSame("hoge");
    }

    @Test(expected = NotProjectMemberException.class)
    public void testChangeOwnerException() throws Exception {
        projectService.createProject("project1", "project1", user1);
        Assertions.assertThat(projectService.findById("project1").map(p -> p.ownerUserId)).isNotEmpty().containsSame(user1.id);

        projectService.changeProjectOwner("project1", user2.id);
        Assertions.assertThat(projectService.findById("project1").map(p -> p.ownerUserId)).isNotEmpty().containsSame(user2.id);
    }

    @Test
    public void testChangeOwner() throws Exception {
        projectService.createProject("project1", "project1", user1);
        projectService.joinToProject("project1", user2.id);
        Assertions.assertThat(projectService.findById("project1").map(p -> p.ownerUserId)).isNotEmpty().containsSame(user1.id);

        projectService.changeProjectOwner("project1", user2.id);
        Assertions.assertThat(projectService.findById("project1").map(p -> p.ownerUserId)).isNotEmpty().containsSame(user2.id);

        //botユーザーはオーナーになれない
        Assertions.assertThatThrownBy(() -> projectService.changeProjectOwner("project1", BotUserUtil.toBotUserId("project1")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testDelete() throws Exception {
        projectService.createProject("project1", "project1", user1);
        projectService.joinToProject("project1", user2.id);

        Assertions.assertThat(projectService.findById("project1")).isNotEmpty();
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(2);

        projectService.deleteProject("project1");
        Assertions.assertThat(projectService.findById("project1")).isEmpty();
        Assertions.assertThat(projectService.findProjectMember("project1")).hasSize(0);
    }

    @Test
    public void testJoined() throws Exception {
        projectService.createProject("project1", "project1", user1);

        Assertions.assertThat(projectService.isJoined("project1", user1.id)).isTrue();
        Assertions.assertThat(projectService.isJoined("project1", user2.id)).isFalse();
        Assertions.assertThat(projectService.isJoined("hoge", user1.id)).isFalse();
        Assertions.assertThat(projectService.isJoined("project1", "hoge")).isFalse();
        Assertions.assertThat(projectService.isJoined("project1", BotUserUtil.toBotUserId("project1"))).isTrue();

    }
}
