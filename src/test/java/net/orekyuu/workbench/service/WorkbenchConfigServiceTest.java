package net.orekyuu.workbench.service;

import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.service.impl.WorkbenchConfigServiceImpl;
import net.orekyuu.workbench.util.TestRepositoryUtil;
import org.assertj.core.api.Assertions;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkbenchConfigServiceTest {

    @Autowired
    private WorkbenchConfigService workbenchConfigService;

    @Autowired
    private TestRepositoryUtil testRepositoryUtil;


    @Before
    public void before() throws UserExistsException, IOException, ProjectExistsException {
        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();
    }

    @Test
    public void findConfig() throws IOException, GitAPIException, ProjectNotFoundException {
        final ArrayList<String> hashList = new ArrayList<>();
        String config =
            "{ " +
                "\"buildSettings\": {" +
                    "\"buildCommand\": [" +
                    "\"gradlew clean\"," +
                    "\"gradlew build\"" +
                    "]," +
                    "\"artifactPath\": [" +
                    "\"build/libs/hoge.jar\"" +
                    "]" +
                "}," +
                "\"testSettings\": {" +
                    "\"testCommand\": [\"gradlew test\"]" +
                "}" +
            "}";
        testRepositoryUtil.createMockRepository("test", (git, root) -> {
            try (OutputStream os = Files.newOutputStream(root.resolve(WorkbenchConfigServiceImpl.WORKBENCH_CONFIG_PATH), StandardOpenOption.CREATE_NEW);
                 OutputStreamWriter writer = new OutputStreamWriter(os)
            ) {
                writer.append(config);
                writer.flush();
                writer.close();

                git.add().addFilepattern(WorkbenchConfigServiceImpl.WORKBENCH_CONFIG_PATH).call();
                String hash = git.commit().setMessage("test commit").call().getId().name();
                hashList.add(hash);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            root.resolve(WorkbenchConfigServiceImpl.WORKBENCH_CONFIG_PATH).toFile().delete();
        });
        WorkbenchConfig test = workbenchConfigService.find("test", hashList.get(0)).orElseThrow(FileSystemNotFoundException::new);
        Assertions.assertThat(test.getBuildSettings().getBuildCommand()).containsOnly("gradlew clean", "gradlew build");
        Assertions.assertThat(test.getBuildSettings().getArtifactPath()).containsOnly("build/libs/hoge.jar");
        Assertions.assertThat(test.getTestSettings().getTestCommand()).containsOnly("gradlew test");
    }

    @Test
    public void testConfigNotFound() throws Exception {
        final ArrayList<String> hashList = new ArrayList<>();
        String config =
            "{ " +
                "\"buildSettings\": {" +
                "\"buildCommand\": [" +
                "\"gradlew clean\"," +
                "\"gradlew build\"" +
                "]," +
                "\"artifactPath\": [" +
                "\"build/libs/hoge.jar\"" +
                "]" +
                "}," +
                "\"testSettings\": {" +
                "\"testCommand\": [\"gradlew test\"]" +
                "}" +
                "}";
        testRepositoryUtil.createMockRepository("test", (git, root) -> {
            try (OutputStream os = Files.newOutputStream(root.resolve("hoge"), StandardOpenOption.CREATE_NEW);
                 OutputStreamWriter writer = new OutputStreamWriter(os)
            ) {
                writer.append(config);
                writer.flush();
                writer.close();

                git.add().addFilepattern(WorkbenchConfigServiceImpl.WORKBENCH_CONFIG_PATH).call();
                String hash = git.commit().setMessage("test commit").call().getId().name();
                hashList.add(hash);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            root.resolve(WorkbenchConfigServiceImpl.WORKBENCH_CONFIG_PATH).toFile().delete();
        });

        Optional<WorkbenchConfig> configOptional = workbenchConfigService.find("test", hashList.get(0));
        Assertions.assertThat(configOptional).isEmpty();
    }
}
