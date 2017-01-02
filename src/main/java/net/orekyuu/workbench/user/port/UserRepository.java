package net.orekyuu.workbench.user.port;

import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.domain.model.UserSettings;
import net.orekyuu.workbench.user.port.table.*;
import net.orekyuu.workbench.user.util.BotUserUtil;
import net.orekyuu.workbench.user.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class UserRepository {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final UserSettingDao userSettingDao;
    private final UserAvatarDao avatarDao;

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(PasswordEncoder passwordEncoder, UserDao userDao, UserSettingDao userSettingDao, UserAvatarDao avatarDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.userSettingDao = userSettingDao;
        this.avatarDao = avatarDao;
    }

    @Transactional(readOnly = false)
    public User createUser(String id, String name, String rawPassword, boolean admin) throws UserExistsException {
        UserTable table = new UserTable(id, name, passwordEncoder.encode(rawPassword), admin);
        if (BotUserUtil.isBotUserId(id)) {
            throw new IllegalArgumentException("botユーザーは作成できません");
        }

        try {
            UserTable result = userDao.insert(table).getEntity();

            User user = UserUtil.fromTable(result);

            avatarDao.insert(new UserAvatarTable(user.getId(), UserUtil.loadDefaultUserIcon()));
            logger.info("User created: " + user);
            return user;
        } catch (DuplicateKeyException e) {
            throw new UserExistsException(id, e);
        }
    }

    public Optional<User> findById(String id) {
        return findById(id, false);
    }

    public Optional<User> findById(String id, boolean fetchSettings) {
        return userDao.findById(id).map(table -> {
            if (fetchSettings) {
                UserSettingTable settingTable = userSettingDao.findById(table.getId())
                    .orElseThrow(() -> new RuntimeException("設定が見つからない"));
                return UserUtil.fromTable(table, settingTable);
            }
            return UserUtil.fromTable(table);
        });
    }

    public List<User> findAll(boolean includeBot) {
        return userDao.selectAll(stream -> stream
            .map(UserUtil::fromTable)
            .filter(user -> includeBot || !user.isBotUser())
            .collect(Collectors.toList()));
    }

    @Transactional(readOnly = false)
    public User save(User user) {
        UserTable table = userDao.update(new UserTable(user.getId(), user.getName(), null, user.getEmail(), user.isAdmin())).getEntity();
        UserSettings settings = user.getUserSettings();
        if (settings != null) {
            userSettingDao.update(new UserSettingTable(user.getId(), settings.isUseGravatar()));
        }
        User result = UserUtil.fromTable(table);
        logger.info("User updated: " + result);
        return result;
    }
}
