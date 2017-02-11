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

    private static final String AUTH_HEADER_NAME = "WWW-Authenticate";
    private static final String AUTH_HEADER_VALUE = "BASIC realm=\"Workbench\"";

    public GitActivityFilter(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
        ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String authorization = req.getHeader("Authorization");
        if (authorization == null) {
            res.addHeader(AUTH_HEADER_NAME, AUTH_HEADER_VALUE);
            res.sendError(401);
            return;
        }

        String decodeHeader = decode(authorization);
        String[] split = decodeHeader.split(":");
        String userName = split[0];
        String uri = req.getRequestURI();
        String uri2 = uri.substring("/git/repos/".length());
        if (uri2.indexOf("git-receive-pack") > 0) {
            UserUsecase userUsecase = context.getBean(UserUsecase.class);
            Optional<User> userOpt = userUsecase.findById(userName);
            ActivityUsecase activityUsecase = context.getBean(ActivityUsecase.class);
            String[] temp = uri2.split("/");
            String projectId = temp[0];
            ProjectUsecase projectUsecase = context.getBean(ProjectUsecase.class);
            projectUsecase.findById(projectId)
                .ifPresent((project) -> {
                    userOpt.ifPresent((user) -> {
                        activityUsecase.createPushActivity(project, user);
                    });
                });
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


    private String decode(String authorizationHeader) {
        String str = authorizationHeader.substring("BASIC ".length());
        return new String(Base64.getDecoder()
            .decode(str));
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

}
