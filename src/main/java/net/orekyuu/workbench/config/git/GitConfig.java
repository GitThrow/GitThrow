package net.orekyuu.workbench.config.git;

import org.eclipse.jgit.http.server.GitServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GitConfig {

    @Bean
    public ServletRegistrationBean gitServlet() {
        GitServlet servlet = new GitServlet();
        servlet.setRepositoryResolver(new WorkbenchGitRepositoryResolver());
        return new ServletRegistrationBean(servlet, "/git/repos/*");
    }
}
