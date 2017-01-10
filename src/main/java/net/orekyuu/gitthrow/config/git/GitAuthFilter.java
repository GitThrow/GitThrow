package net.orekyuu.gitthrow.config.git;

import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.context.ApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class GitAuthFilter implements Filter {

    private final ApplicationContext context;

    private static final String AUTH_HEADER_NAME = "WWW-Authenticate";
    private static final String AUTH_HEADER_VALUE = "BASIC realm=\"Workbench\"";

    public GitAuthFilter(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String authorization = req.getHeader("Authorization");
        if (authorization == null) {
            res.addHeader(AUTH_HEADER_NAME, AUTH_HEADER_VALUE);
            res.sendError(401);
            return;
        }

        String decodeHeader = decode(authorization);
        String[] split = decodeHeader.split(":");
        String userName = split[0];
        String pw = split[1];

        UserUsecase userUsecase = context.getBean(UserUsecase.class);
        Optional<User> userOpt = userUsecase.findById(userName);
        boolean auth = userOpt
            .map(user -> userUsecase.matchPassword(user, pw))
            .orElse(false);

        if (!auth) {
            res.addHeader(AUTH_HEADER_NAME, AUTH_HEADER_VALUE);
            res.sendError(401);
            return;
        }

        User user = userOpt.orElseThrow(NullPointerException::new);
        ProjectUsecase projectUsecase = context.getBean(ProjectUsecase.class);

        String projectId = getProjectId(req.getRequestURI());
        //参加していない
        if (projectId == null || !projectUsecase.isJoined(projectId, user.getId())) {
            res.sendError(403, "リポジトリにアクセスする権限がありません");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    private String getProjectId(String path) {
        String[] split = path.split("/");
        if (split.length < 3) {
            return null;
        }
        return split[3];
    }

    private String decode(String authorizationHeader) {
        String str = authorizationHeader.substring("BASIC ".length());
        return new String(Base64.getDecoder().decode(str));
    }

    @Override
    public void destroy() {

    }
}
