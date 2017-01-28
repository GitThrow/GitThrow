package net.orekyuu.gitthrow.util;


import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public abstract class UsecaseTestBase {

    @Autowired
    protected ProjectUsecase projectUsecase;
    @Autowired
    protected UserUsecase userUsecase;

    protected User user1;
    protected User user2;
    protected Project project;

    @Autowired
    private ProjectTestUtil util;

    @Before
    public void setUp() throws Exception {
        user1 = userUsecase.create("user1", "user1", "password");
        user2 = userUsecase.create("user2", "user2", "password");

        project = projectUsecase.createProject("project1", "project1", user1);
        projectUsecase.join(project, user2);

        onSetup();
    }

    protected void onSetup() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        util.deleteGitRepositoryAndWorkspaceDir();
        onTearDown();
    }

    protected void onTearDown() throws Exception {
    }
}
