package net.orekyuu.gitthrow.config.git;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import net.orekyuu.gitthrow.activity.usecase.ActivityUsecase;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;

public class GitActivityFilter implements Filter {
    private ApplicationContext context;

    public GitActivityFilter(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
        ServletException {

        filterChain.doFilter(servletRequest, servletResponse);
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;


        String uri = req.getRequestURI();
        String subUri = uri.substring("/git/repos/".length());
        if (subUri.indexOf("git-receive-pack") < 0) {
            return;
        }
        String authorization = req.getHeader("Authorization");
        if (authorization == null) {
            return;
        }

        if (res.getStatus() == 200) {
            String[] temp = subUri.split("/");
            if (temp.length == 2) {
                ActivityUsecase activityUsecase = context.getBean(ActivityUsecase.class);
                ProjectUsecase projectUsecase = context.getBean(ProjectUsecase.class);
                UserUsecase userUsecase = context.getBean(UserUsecase.class);
                
                String projectId = temp[0];
                String decodeHeader = decode(authorization);
                String[] split = decodeHeader.split(":");
                String userName = split[0];
                
                Optional<User> userOpt = userUsecase.findById(userName);
                projectUsecase.findById(projectId)
                    .ifPresent((project) -> {
                        userOpt.ifPresent((user) -> {
                            activityUsecase.createPushActivity(project, user);
                        });
                    });
            }
        }
    }


    private String decode(String authorizationHeader) {
        String str = authorizationHeader.substring("BASIC ".length());
        return new String(Base64.getDecoder()
            .decode(str));
    }

    @Override
    public void init(FilterConfig config) throws ServletException {}

}
