package net.orekyuu.gitthrow.config.git;

import net.orekyuu.gitthrow.git.hooks.event.PostReceiveHookEvent;
import net.orekyuu.gitthrow.git.hooks.event.PreReceiveHookEvent;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.http.server.resolver.DefaultReceivePackFactory;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Configuration
public class GitConfig {

    @Autowired
    private ApplicationContext context;
    @Value("${net.orekyuu.workbench.repository-dir}")
    private String gitDir;
    @Autowired
    private ApplicationEventPublisher publisher;
    private static final Logger logger = LoggerFactory.getLogger(GitConfig.class);

    @Bean
    public ServletRegistrationBean gitServlet() {
        GitServlet servlet = new GitServlet();
        servlet.setRepositoryResolver(new WorkbenchGitRepositoryResolver(gitDir));
        servlet.setReceivePackFactory(new DefaultReceivePackFactory() {
            @Override
            public ReceivePack create(HttpServletRequest req, Repository db) throws ServiceNotEnabledException, ServiceNotAuthorizedException {
                ReceivePack receivePack = super.create(req, db);

                //git-receive-pack以外のリクエストも来るのでここで弾いておく
                //その他のリクエスト例: /git/repos/hoge/info/refs
                Pattern pattern = Pattern.compile("^/git/repos/(.+)/git-receive-pack");
                Matcher matcher = pattern.matcher(req.getRequestURI());
                if (!matcher.find()) {
                    return receivePack;
                }
                String projectName = matcher.group(1);

                ProjectUsecase usecase = context.getBean(ProjectUsecase.class);
                usecase.findById(projectName).ifPresent(project -> {
                    receivePack.setPreReceiveHook((pack, commands) -> {
                        publisher.publishEvent(new PreReceiveHookEvent(this, pack, commands, project));
                    });
                    receivePack.setPostReceiveHook((pack, commands) -> {
                        publisher.publishEvent(new PostReceiveHookEvent(this, pack, commands, project));
                    });
                });
                return receivePack;
            }
        });
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
