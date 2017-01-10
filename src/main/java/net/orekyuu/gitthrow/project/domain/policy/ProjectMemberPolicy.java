package net.orekyuu.gitthrow.project.domain.policy;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.util.BotUserUtil;
import net.orekyuu.gitthrow.util.DomainPolicy;

public class ProjectMemberPolicy implements DomainPolicy<User> {

    private final Project project;
    public ProjectMemberPolicy(Project project) {
        this.project = project;
    }

    @Override
    public boolean check(User user) {
        return !BotUserUtil.isBotUserId(user.getId()) || BotUserUtil.isProjectBot(project.getId(), user.getId());
    }
}
