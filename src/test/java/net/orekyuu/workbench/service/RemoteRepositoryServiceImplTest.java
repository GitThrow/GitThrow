//package net.orekyuu.workbench.service;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.ArrayList;
//
//import org.assertj.core.api.Assertions;
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.eclipse.jgit.api.errors.NoFilepatternException;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
//import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
//import net.orekyuu.workbench.service.exceptions.UserExistsException;
//import net.orekyuu.workbench.util.TestRepositoryUtil;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class RemoteRepositoryServiceImplTest {
//
//    @Autowired
//    private RemoteRepositoryService remoteRepositoryService;
//
//    @Autowired
//    private TestRepositoryUtil testRepositoryUtil;
//
//
//    @Before
//    public void before() throws UserExistsException, IOException, ProjectExistsException {
//        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();
//    }
//
//    @Test
//    public void findContentWithHash() throws IOException, NoFilepatternException, GitAPIException, ProjectNotFoundException {
//        final ArrayList<String> hashList = new ArrayList<>();
//        testRepositoryUtil.createMockRepository("test", (git, root) -> {
//            try (OutputStream os = Files.newOutputStream(root.resolve("README.md"), StandardOpenOption.CREATE_NEW)) {
//                Files.copy(Paths.get("README.md"), os);
//                git.add().addFilepattern("README.md").call();
//                String hash = git.commit().setMessage("test commit").call().getId().name();
//                hashList.add(hash);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            root.resolve("README.md").toFile().delete();
//        });
//        remoteRepositoryService.getRepositoryFile("test", hashList.get(0), "README.md").orElseThrow(FileNotFoundException::new);
//    }
//
//    @Test
//    public void findContentWithBranch() throws IOException, NoFilepatternException, GitAPIException, ProjectNotFoundException {
//        testRepositoryUtil.createMockRepository("test", (git, root) -> {
//            try (OutputStream os = Files.newOutputStream(root.resolve("README.md"), StandardOpenOption.CREATE_NEW)) {
//                Files.copy(Paths.get("README.md"), os);
//                git.add().addFilepattern("README.md").call();
//                git.commit().setMessage("test commit").call().getId().name();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            root.resolve("README.md").toFile().delete();
//        });
//        remoteRepositoryService.getRepositoryFile("test", "master", "README.md").orElseThrow(FileNotFoundException::new);
//    }
//
//    @Test
//    public void findContentWithTag() throws IOException, NoFilepatternException, GitAPIException, ProjectNotFoundException {
//        testRepositoryUtil.createMockRepository("test", (git, root) -> {
//            try (OutputStream os = Files.newOutputStream(root.resolve("README.md"), StandardOpenOption.CREATE_NEW)) {
//                Files.copy(Paths.get("README.md"), os);
//                git.add().addFilepattern("README.md").call();
//                git.commit().setMessage("test commit").call().getId().name();
//                git.tag().setName("test").call();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            root.resolve("README.md").toFile().delete();
//        });
//        remoteRepositoryService.getRepositoryFile("test", "test", "README.md").orElseThrow(FileNotFoundException::new);
//    }
//
//    @Test
//    public void findContentSubFolder() throws IOException, NoFilepatternException, GitAPIException, ProjectNotFoundException {
//        String pathStr="somefolder/README.md";
//        testRepositoryUtil.createMockRepository("test", (git, root) -> {
//            Path filePath=root.resolve(pathStr);
//            filePath.getParent().toFile().mkdir();
//            try (OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)) {
//                Files.copy(Paths.get("README.md"), os);
//                git.add().addFilepattern(pathStr).call();
//                git.commit().setMessage("test commit").call().getId().name();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            filePath.toFile().delete();
//        });
//        remoteRepositoryService.getRepositoryFile("test", "master", pathStr).orElseThrow(FileNotFoundException::new);
//    }
//
//    @Test
//    public void findWrongObjectCase() throws IOException, NoFilepatternException, GitAPIException, ProjectNotFoundException {
//        String pathStr="somefolder/README.md";
//        testRepositoryUtil.createMockRepository("test", (git, root) -> {
//            Path filePath=root.resolve(pathStr);
//            filePath.getParent().toFile().mkdir();
//            try (OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)) {
//                Files.copy(Paths.get("README.md"), os);
//                git.add().addFilepattern(pathStr).call();
//                git.commit().setMessage("test commit").call().getId().name();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            filePath.toFile().delete();
//        });
//        try{
//            remoteRepositoryService.getRepositoryFile("test", "aaa", pathStr).orElseThrow(FileNotFoundException::new);
//            Assertions.fail("should not reach here");
//        }catch(FileNotFoundException e){
//        }
//    }
//
//    @Test
//    public void findWrongObjectCase2() throws IOException, NoFilepatternException, GitAPIException, ProjectNotFoundException {
//        String pathStr="somefolder/README.md";
//        testRepositoryUtil.createMockRepository("test", (git, root) -> {});
//        try{
//            remoteRepositoryService.getRepositoryFile("test", "master", pathStr).orElseThrow(FileNotFoundException::new);
//            Assertions.fail("should not reach here");
//        }catch(FileNotFoundException e){
//        }
//    }
//}
