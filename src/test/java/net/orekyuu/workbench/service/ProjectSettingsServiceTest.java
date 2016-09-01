package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.BuildSettings;
import net.orekyuu.workbench.entity.TestSettings;
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

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProjectSettingsServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectSettingService projectSettingService;

    @Autowired
    private TestRepositoryUtil testRepositoryUtil;

    @Before
    public void before() throws UserExistsException, IOException, ProjectExistsException {
        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();

        userService.createUser("user1", "user1", "pw");
        userService.createUser("user2", "user2", "pw");

        User user1 = userService.findById("user1").get();

        projectService.createProject("project1", "project1", user1);

    }

    @Test
    public void testFind() {
        Assertions.assertThat(projectSettingService.findBuildSettings("project1").isPresent()).isTrue();
        Assertions.assertThat(projectSettingService.findTestSettings("project1").isPresent()).isTrue();
    }

    @Test
    public void testUpdate() {
        BuildSettings buildSettings = projectSettingService.findBuildSettings("project1").get();
        buildSettings.buildCommand = "gradlew build";
        buildSettings.artifactPath = "/hoge/*.jar";
        projectSettingService.updateBuildSettings(buildSettings);
        buildSettings = projectSettingService.findBuildSettings("project1").get();
        Assertions.assertThat(buildSettings.buildCommand).isEqualTo("gradlew build");
        Assertions.assertThat(buildSettings.artifactPath).isEqualTo("/hoge/*.jar");

        TestSettings testSettings = projectSettingService.findTestSettings("project1").get();
        testSettings.testCommand = "gradlew test";
        testSettings.xmlPath = "*.xml";
        projectSettingService.updateTestSettings(testSettings);
        testSettings = projectSettingService.findTestSettings("project1").get();
        Assertions.assertThat(testSettings.testCommand).isEqualTo("gradlew test");
        Assertions.assertThat(testSettings.xmlPath).isEqualTo("*.xml");

    }
}
