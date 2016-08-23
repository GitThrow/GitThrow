package net.orekyuu.workbench.config.git;

import org.eclipse.jgit.http.server.GitServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GitConfig {

    @Autowired
    private ApplicationContext context;
    @Value("${net.orekyuu.workbench.repository-dir}")
    private String gitDir;

    @Bean
    public ServletRegistrationBean gitServlet() {
        GitServlet servlet = new GitServlet();
        servlet.setRepositoryResolver(new WorkbenchGitRepositoryResolver(gitDir));
        return new ServletRegistrationBean(servlet, "/git/repos/*");
    }

    @Bean
    public FilterRegistrationBean gitAuthFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new GitAuthFilter(context));
        registration.addUrlPatterns("/git/repos/*");
        registration.setName("gitAuthFilter");
        return registration;
    }
}
