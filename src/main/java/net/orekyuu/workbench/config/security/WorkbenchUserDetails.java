package net.orekyuu.workbench.config.security;


import net.orekyuu.workbench.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class WorkbenchUserDetails extends org.springframework.security.core.userdetails.User {

    private final User user;

    public WorkbenchUserDetails(User user) {
        super(user.id, user.password, AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
