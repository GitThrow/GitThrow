package net.orekyuu.workbench.project.domain.policy;

import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.util.BotUserUtil;
import net.orekyuu.workbench.util.DomainPolicy;

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
