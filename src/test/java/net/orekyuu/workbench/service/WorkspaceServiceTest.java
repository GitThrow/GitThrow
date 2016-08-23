package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.util.TestRepositoryUtil;
import org.assertj.core.api.Assertions;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WorkspaceServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @Autowired
    private WorkspaceService workspaceService;

    private User user1;

    @Autowired
    private TestRepositoryUtil testRepositoryUtil;

    @Before
    public void before() throws UserExistsException, IOException {
        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();

        userService.createUser("user1", "user1", "pw");

        user1 = userService.findById("user1").get();

        try {
            projectService.createProject("project1", "project1", user1);
        } catch (ProjectExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() throws GitAPIException, ProjectNotFoundException {
        Path workspaceDir = workspaceService.getProjectWorkspaceDir("project1");

        Assertions.assertThat(Files.exists(workspaceDir)).isFalse();
        workspaceService.update("project1");
        Assertions.assertThat(Files.exists(workspaceDir)).isTrue();
        workspaceService.update("project1");
        Assertions.assertThat(Files.exists(workspaceDir)).isTrue();
    }

    @Test
    public void testDelete() throws GitAPIException, ProjectNotFoundException {
        Path workspaceDir = workspaceService.getProjectWorkspaceDir("project1");
        workspaceService.update("project1");
        Assertions.assertThat(Files.exists(workspaceDir)).isTrue();

        workspaceService.delete("project1");
        Assertions.assertThat(Files.exists(workspaceDir)).isFalse();
    }
}
