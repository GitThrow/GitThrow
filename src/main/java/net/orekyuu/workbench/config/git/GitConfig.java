package net.orekyuu.workbench.config.git;

import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.UploadPack;
import org.eclipse.jgit.transport.resolver.FileResolver;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.servlet.http.HttpServletRequest;


@Configuration
public class GitConfig {

    @Bean
    @Scope("singleton")
    public ServletRegistrationBean gitServlet() {
        GitServlet servlet = new GitServlet();
        servlet.setRepositoryResolver(new WorkbenchGitRepositoryResolver());
        return new ServletRegistrationBean(servlet, "/git/repos/*");
    }
}
