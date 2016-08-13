package net.orekyuu.workbench.config.git;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.FileResolver;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

public class WorkbenchGitRepositoryResolver implements RepositoryResolver<HttpServletRequest> {

    public static final Path REPOSITORIES_DIR = Paths.get("repos");

    private FileResolver<HttpServletRequest> resolver = new FileResolver<>(REPOSITORIES_DIR.toFile(), true);
    @Override
    public Repository open(HttpServletRequest req, String repoName) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
        try {
            return resolver.open(req, repoName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
