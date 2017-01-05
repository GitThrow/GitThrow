package net.orekyuu.workbench.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Component
public class ProjectTestUtil {

    @Value("${net.orekyuu.workbench.repository-dir}")
    private String gitDir;
    @Value("${net.orekyuu.workbench.workspace-dir}")
    private String workspaceDir;

    public void deleteGitRepositoryAndWorkspaceDir() {
        deleteDir(gitDir);
        deleteDir(workspaceDir);
    }

    private void deleteDir(String dir) {
        Path path = Paths.get(dir);
        if (Files.notExists(path)) {
            return;
        }
        try {

            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    Files.setAttribute(file, "dos:readonly",false);
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }
}
