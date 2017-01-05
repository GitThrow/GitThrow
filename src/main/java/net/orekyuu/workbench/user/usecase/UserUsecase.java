package net.orekyuu.workbench.user.usecase;

import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.port.UserRepository;
import net.orekyuu.workbench.user.port.table.UserAvatarDao;
import net.orekyuu.workbench.user.port.table.UserAvatarTable;
import net.orekyuu.workbench.user.port.table.UserDao;
import net.orekyuu.workbench.user.port.table.UserTable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserUsecase {

    private final UserRepository userRepository;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    private final UserAvatarDao userAvatarDao;


    public UserUsecase(UserRepository userRepository, UserDao userDao, PasswordEncoder passwordEncoder, UserAvatarDao userAvatarDao) {
        this.userRepository = userRepository;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.userAvatarDao = userAvatarDao;
    }

    @Transactional(readOnly = false)
    public User create(String id, String name, String rawPassword, boolean isAdmin) throws UserExistsException {
        return userRepository.createUser(id, name, rawPassword, isAdmin);
    }

    @Transactional(readOnly = false)
    public User create(String id, String name, String rawPassword) throws UserExistsException {
        return create(id, name, rawPassword, false);
    }

    @Transactional(readOnly = false)
    public void changePassword(User user, String password, String newPassword) {
        UserTable table = userDao.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("存在しないユーザーです: " + user.getId()));
        if (!passwordEncoder.matches(password, table.getPassword())) {
            throw new PasswordNotMatchException();
        }
        userDao.update(new UserTable(user.getId(), user.getName(), passwordEncoder.encode(newPassword), user.getEmail(), user.isAdmin()));
    }

    @Transactional(readOnly = false)
    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findById(String id, boolean fetchSettings) {
        return userRepository.findById(id, fetchSettings);
    }

    public List<User> findAll(boolean includeBot) {
        return userRepository.findAll(includeBot);
    }

    public byte[] findAvater(String userId) {
        return userAvatarDao.findById(userId)
            .map(UserAvatarTable::getAvatar)
            .orElseThrow(() -> new RuntimeException("アバターが見つかりません"));
    }

    public void updateAvater(User user, byte[] avater) {
        userAvatarDao.update(new UserAvatarTable(user.getId(), avater));
    }

    public boolean matchPassword(User user, String password) {
        UserTable table = userDao.findById(user.getId())
            .orElseThrow(() -> new IllegalArgumentException("存在しないユーザーです: " + user.getId()));
        return passwordEncoder.matches(password, table.getPassword());
    }
}
