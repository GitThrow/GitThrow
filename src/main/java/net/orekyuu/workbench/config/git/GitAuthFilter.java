package net.orekyuu.workbench.config.git;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import sun.misc.BASE64Decoder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

public class GitAuthFilter implements Filter {

    private final ApplicationContext context;

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
            res.addHeader("WWW-Authenticate", "BASIC realm=\"Workbench\"");
            res.sendError(401);
            return;
        }

        String decodeHeader = decode(authorization);
        String[] split = decodeHeader.split(":");
        String userName = split[0];
        String pw = split[1];

        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        UserService userService = context.getBean(UserService.class);
        Optional<User> userOpt = userService.findById(userName);
        boolean auth = userOpt
            .map(u -> u.password)
            .map(userPW -> passwordEncoder.matches(pw, userPW))
            .orElse(false);

        if (!auth) {
            res.addHeader("WWW-Authenticate", "BASIC realm=\"Workbench\"");
            res.sendError(401);
            return;
        }

        User user = userOpt.orElseThrow(NullPointerException::new);
        ProjectService projectService = context.getBean(ProjectService.class);

        String projectId = getProjectId(req.getRequestURI());
        //参加していない
        if (projectId == null || !projectService.isJoined(projectId, user.id)) {
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
        try {
            return new String(new BASE64Decoder().decodeBuffer(str));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
