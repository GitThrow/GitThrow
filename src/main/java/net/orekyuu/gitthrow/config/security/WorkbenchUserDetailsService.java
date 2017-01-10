package net.orekyuu.gitthrow.config.security;

import net.orekyuu.gitthrow.user.port.table.UserDao;
import net.orekyuu.gitthrow.user.port.table.UserTable;
import net.orekyuu.gitthrow.user.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkbenchUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserTable> optional = userDao.findById(username);
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        UserTable table = optional.get();
        return new WorkbenchUserDetails(UserUtil.fromTable(table), table.getPassword());
    }
}
