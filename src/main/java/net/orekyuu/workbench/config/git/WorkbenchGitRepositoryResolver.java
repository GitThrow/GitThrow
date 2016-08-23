package net.orekyuu.workbench.config.git;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.FileResolver;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class WorkbenchGitRepositoryResolver implements RepositoryResolver<HttpServletRequest> {

    private final FileResolver<HttpServletRequest> resolver;

    WorkbenchGitRepositoryResolver(String reposDir) {
        this.resolver = new FileResolver<>(new File(reposDir), true);
    }

    @Override
    public Repository open(HttpServletRequest req, String repoName) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
        try {
            return resolver.open(req, repoName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
