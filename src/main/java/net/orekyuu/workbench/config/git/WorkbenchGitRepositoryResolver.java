package net.orekyuu.workbench.config.git;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkbenchGitRepositoryResolver implements RepositoryResolver<HttpServletRequest> {

    private static final Path REPOSITORIES_DIR = Paths.get("repos");
    @Override
    public Repository open(HttpServletRequest req, String repoName) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
        Path repositoryDir = REPOSITORIES_DIR.resolve(repoName);
        try {
            Repository repository = FileRepositoryBuilder.create(repositoryDir.toFile());
            repository.incrementOpen();
            return repository;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
