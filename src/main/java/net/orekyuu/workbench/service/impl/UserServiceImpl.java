package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.entity.dao.UserDao;
import net.orekyuu.workbench.service.UserService;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = false)
    @Override
    public void createUser(String id, String name, String rawPassword, boolean admin) throws UserExistsException {
        User user = new User(id, name, passwordEncoder.encode(rawPassword), admin);
        try {
            userDao.insert(user);
        } catch (UniqueConstraintException e) {
            //ユーザーが存在してるので失敗
            throw new UserExistsException(id, e);
        }
    }

    @Override
    public Optional<User> findById(String id) {
        return userDao.findById(id);
    }
}
