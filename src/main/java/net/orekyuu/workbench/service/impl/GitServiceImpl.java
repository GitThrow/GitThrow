package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.service.GitService;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class GitServiceImpl implements GitService {

    @Autowired
    private Properties applicationProperties;

    @Value("${net.orekyuu.workbench.repository-dir}")
    private String gitDir;

    @Override
    public Path getProjectGitRepositoryDir(String projectId) {
        String dir = gitDir;
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return Paths.get(dir + projectId);
    }

    @Override
    public void createRemoteRepository(String projectId) {
        Path repositoryDir = getProjectGitRepositoryDir(projectId);
        if (Files.exists(repositoryDir)) {
            IOException exception = new IOException("すでに存在している: " + projectId);
            throw new UncheckedIOException(exception);
        }

        try {
            Files.createDirectories(repositoryDir);

            Repository repo = new FileRepositoryBuilder()
                .setGitDir(repositoryDir.toFile())
                .setBare()
                .build();
            final boolean isBare = true;
            repo.create(isBare);

            StoredConfig config = repo.getConfig();
            config.setBoolean("http", null, "receivepack", true);
            config.save();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
