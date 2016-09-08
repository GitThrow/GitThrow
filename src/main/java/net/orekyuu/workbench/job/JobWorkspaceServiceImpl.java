package net.orekyuu.workbench.job;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Value;

public class JobWorkspaceServiceImpl implements JobWorkspaceService {

    @Value("${net.orekyuu.workbench.job-workspace-dir}")
    private String jobDir;

    @Override
    public Path getWorkspacePath(UUID jobId) {
        return Paths.get(jobDir, jobId.toString());
    }

    @Override
    public Repository getRepository(UUID jobId) throws IOException {
        Repository repository = new FileRepositoryBuilder()
            .setWorkTree(getWorkspacePath(jobId).toFile())
            .build();
        repository.incrementOpen();
        return repository;
    }

    @Override
    public void createWorkspaceIfNotExists(UUID jobId) throws IOException {
        Path workspacePath = getWorkspacePath(jobId);
        if (Files.notExists(workspacePath)) {
            Files.createDirectories(workspacePath);
        }
    }

    @Override
    public void deleteWorkspaceIfExists(UUID jobId) throws IOException {
        Path workspacePath = getWorkspacePath(jobId);
        if (Files.exists(workspacePath)) {
            FileUtils.deleteDirectory(workspacePath.toFile());
        }
    }
}
