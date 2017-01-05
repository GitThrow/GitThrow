//package net.orekyuu.workbench.util;
//
//import java.io.IOException;
//import java.io.UncheckedIOException;
//import java.nio.file.FileVisitResult;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.SimpleFileVisitor;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.util.function.BiConsumer;
//
//import org.eclipse.jgit.api.Git;
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.eclipse.jgit.lib.Repository;
//import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import net.orekyuu.workbench.service.RemoteRepositoryService;
//
//@Component
//public class TestRepositoryUtil {
//
//    @Autowired
//    private RemoteRepositoryService remoteRepositoryService;
//
//    @Value("${net.orekyuu.workbench.repository-dir}")
//    private String gitDir;
//    @Value("${net.orekyuu.workbench.workspace-dir}")
//    private String workspaceDir;
//
//    public void deleteGitRepositoryAndWorkspaceDir() {
//        deleteDir(gitDir);
//        deleteDir(workspaceDir);
//    }
//
//    private void deleteDir(String dir) {
//        Path path = Paths.get(dir);
//        if (Files.notExists(path)) {
//            return;
//        }
//        try {
//
//            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
//
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    Files.setAttribute(file, "dos:readonly",false);
//                    Files.delete(file);
//                    return FileVisitResult.CONTINUE;
//                }
//
//                @Override
//                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                    Files.delete(dir);
//                    return FileVisitResult.CONTINUE;
//                }
//
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new UncheckedIOException(e);
//        }
//    }
//
//    public void createMockRepository(String projectId, BiConsumer<Git,Path> initializer) throws IOException, IllegalStateException, GitAPIException{
//      Path repo=remoteRepositoryService.getProjectGitRepositoryDir(".git");
//      Path gitDir=repo.getParent();
//      Repository repository=FileRepositoryBuilder.create(repo.toFile());
//      try(Git git=new Git(repository)){
//          git.init().setGitDir(repo.toFile()).setBare(false).call();
//          initializer.accept(git, gitDir);
//          repo.toFile().renameTo(repo.getParent().resolve(projectId).toFile());
//      }
//    }
//}
