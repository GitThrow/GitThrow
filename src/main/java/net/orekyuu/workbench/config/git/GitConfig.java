package net.orekyuu.workbench.config.git;

import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.UploadPack;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GitConfig {

    @Bean
    public ServletRegistrationBean gitServlet() {
        GitServlet servlet = new GitServlet();
        servlet.setRepositoryResolver(new WorkbenchGitRepositoryResolver());
        servlet.setUploadPackFactory((httpServletRequest, repository) -> new UploadPack(repository));
        servlet.setReceivePackFactory((httpServletRequest, repository) -> new ReceivePack(repository));
        return new ServletRegistrationBean(servlet, "/git/repos/*");
    }
}
