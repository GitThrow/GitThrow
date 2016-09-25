package net.orekyuu.workbench.config.security;

import net.orekyuu.workbench.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WorkbenchUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findById(username)
            .filter(user -> !user.isBotUser()) //botユーザーはログインさせない
            .map(WorkbenchUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
