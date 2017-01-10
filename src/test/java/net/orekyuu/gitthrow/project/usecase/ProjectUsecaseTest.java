package net.orekyuu.gitthrow.project.usecase;

import net.orekyuu.gitthrow.git.domain.RemoteRepository;
import net.orekyuu.gitthrow.git.domain.RemoteRepositoryFactory;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.service.exceptions.ProjectExistsException;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import net.orekyuu.gitthrow.user.util.BotUserUtil;
import net.orekyuu.gitthrow.util.PolicyException;
import net.orekyuu.gitthrow.util.ProjectTestUtil;
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

import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProjectUsecaseTest {

    @Autowired
    private ProjectUsecase usecase;
    @Autowired
    private UserUsecase userUsecase;
    @Autowired
    private RemoteRepositoryFactory factory;

    @Autowired
    private ProjectTestUtil util;

    private User user1;
    private User user2;
    private User user3;
    private Project hogeProject;

    public static Condition<Project> testFields(String id, String name, User owner) {
        return new Condition<>(project ->
            project.getId().equals(id) &&
                project.getName().equals(name) &&
                project.getOwner().equals(owner),
            "すべてのフィールドが等しいか");
    }

    @Before
    public void before() throws Exception{
        user1 = userUsecase.create("user1", "user1", "password");
        user2 = userUsecase.create("user2", "user2", "password");
        user3 = userUsecase.create("user3", "user3", "password");
        hogeProject = usecase.createProject("hoge", "Hoge Project", user1);
    }

    @After
    public void after() throws Exception {
        util.deleteGitRepositoryAndWorkspaceDir();
    }

    @Test
    public void testCreateProject() throws Exception {
        Project project = usecase.createProject("projectId", "projectName", user1);
        Assertions.assertThat(project)
            .is(testFields("projectId", "projectName", user1));
        Assertions.assertThat(project.getMember())
            .hasSize(2)
            .contains(user1);

        RemoteRepository repo = factory.create("projectId");
        Assertions.assertThat(repo.getRepositoryDir())
            .exists()
            .isDirectory();
        Assertions.assertThat(Paths.get(repo.getRepositoryDir().toString(), "HEAD"))
            .exists()
            .isRegularFile();

        Assertions.assertThatThrownBy(() -> usecase.createProject("projectId", "project", user1))
            .isInstanceOf(ProjectExistsException.class);
    }

    @Test
    public void testCreateProjectError() throws Exception {
        usecase.createProject("projectId", "projectName", user1);

        Assertions.assertThatThrownBy(() -> usecase.createProject("projectId", "project", user1))
            .isInstanceOf(ProjectExistsException.class);

        Assertions.assertThatThrownBy(() -> usecase.createProject("projectId", "project", user2))
            .isInstanceOf(ProjectExistsException.class);
    }

    @Test
    public void joinMember() {
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(2);

        Project project = usecase.join(hogeProject, user2);

        Assertions.assertThat(hogeProject.getMember()).hasSize(3);
        Assertions.assertThat(project.getMember()).hasSize(3);
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(3);

        Assertions.assertThatThrownBy(() -> usecase.join(hogeProject, user2))
            .isInstanceOf(PolicyException.class);
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(3);
    }

    @Test
    public void withdrawMember() {
        //メンバーの削除
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(2);
        usecase.join(hogeProject, user2);
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(3);
        Project project = usecase.withdraw(hogeProject, user2);
        Assertions.assertThat(project.getMember()).hasSize(2);
        Assertions.assertThat(hogeProject.getMember()).hasSize(2);
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(2);

        //管理者は削除できない
        Assertions.assertThatThrownBy(() -> usecase.withdraw(hogeProject, user1)).isInstanceOf(PolicyException.class);
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(2);
        Assertions.assertThat(hogeProject.getMember()).hasSize(2);

        //botは削除できない
        User bot = userUsecase.findById(BotUserUtil.toBotUserId(hogeProject.getId())).get();
        Assertions.assertThatThrownBy(() -> usecase.withdraw(hogeProject, bot)).isInstanceOf(PolicyException.class);
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(2);
        Assertions.assertThat(hogeProject.getMember()).hasSize(2);
    }

    @Test
    public void testUpdate() {
        usecase.join(hogeProject, user2);
        Project project = usecase.join(hogeProject, user3);

        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(4);

        project.getMember().remove(user2);
        project.renameProject("fuga");
        project.changeProjectOwner(user3);

        Project save = usecase.save(project);
        Assertions.assertThat(save).is(testFields("hoge", "fuga", user3));
        Assertions.assertThat(save.getMember()).hasSize(4);

        Assertions.assertThat(usecase.findById("hoge").get()).is(testFields("hoge", "fuga", user3));
        Assertions.assertThat(usecase.findById("hoge").get().getMember()).hasSize(4);
    }

    @Test
    public void testDelete() {
        usecase.delete(hogeProject);

        RemoteRepository repo = factory.create("hoge");
        Assertions.assertThat(repo.getRepositoryDir()).doesNotExist();

        Assertions.assertThat(usecase.findById("hoge")).isNotPresent();
    }

    @Test
    public void testFindById() {
        Assertions.assertThat(usecase.findById("hoge"))
            .isPresent()
            .map(Project::getId)
            .hasValue("hoge");

        Assertions.assertThat(usecase.findById("fuga")).isNotPresent();
    }

    @Test
    public void testFindProject() {
        Project fugaProject = usecase.createProject("fuga", "fuga", user2);
        usecase.join(fugaProject, user1);

        Assertions.assertThat(usecase.findByUser(user1)).hasSize(2);
        Assertions.assertThat(usecase.findByUser(user2)).hasSize(1);
        Assertions.assertThat(usecase.findByUser(user3)).hasSize(0);
    }

    @Test
    public void testIsJoin() {
        Assertions.assertThat(usecase.isJoined("hoge", user1.getId())).isTrue();
        Assertions.assertThat(usecase.isJoined("fuga", user1.getId())).isFalse();
    }
}
