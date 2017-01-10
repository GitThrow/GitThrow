package net.orekyuu.gitthrow.config.security;


import net.orekyuu.gitthrow.user.domain.model.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class WorkbenchUserDetails extends org.springframework.security.core.userdetails.User {

    private final User user;

    public WorkbenchUserDetails(User user, String password) {
        super(user.getId(), password, AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.user = user;
    }

    /**
     * @return DB上のユーザー情報
     */
    public User getUser() {
        return user;
    }
}
