package net.orekyuu.gitthrow.git.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class RemoteRepositoryFactory {

    @Value("${net.orekyuu.workbench.repository-dir}")
    private String gitDir;

    public RemoteRepository create(String projectId) {
        return new RemoteRepository(projectId, Paths.get(gitDir, projectId));
    }
}
