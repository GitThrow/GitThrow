package net.orekyuu.gitthrow.user.usecase;

import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.domain.model.UserSettings;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserUsecaseTest {

    @Autowired
    private UserUsecase usecase;

    public static Condition<User> testAllFieldMatch(User user) {
        return new Condition<>(u ->
            user.isAdmin() == u.isAdmin() &&
            user.getId().equals(u.getId()) &&
            user.getName().equals(u.getName()) &&
            user.getEmail().equals(u.getEmail()),
            "すべてのフィールドが等しいか");
    }

    public static Condition<User> testFields(String id, String name, String email, boolean admin) {
        return new Condition<>(user ->
            user.getId().equals(id) &&
            user.isAdmin() == admin &&
            user.getName().equals(name) &&
            user.getEmail().equals(email),
            "すべてのフィールドが等しいか");
    }

    public static Condition<UserSettings> testSettingsFields(boolean useGravatar) {
        return new Condition<>(settings ->
            settings.isUseGravatar() == useGravatar,
            "すべてのフィールドが等しいか");
    }

    public List<User> findAllAdminFiltered(boolean includeBot) {
        return usecase.findAll(includeBot).stream()
            .filter(user -> !user.getId().equals("admin"))
            .collect(Collectors.toList());
    }

    @Test
    public void create() throws Exception {
        Assertions.assertThat(findAllAdminFiltered(false)).isEmpty();
        User user = usecase.create("user1", "user_name", "password", false);
        Assertions.assertThat(findAllAdminFiltered(false))
            .hasSize(1)
            .first()
            .is(testFields("user1", "user_name", "", false))
            .is(testAllFieldMatch(user));

        Assertions.assertThat(user.getUserSettings()).isNotNull();
        Assertions.assertThat(usecase.findAvater(user.getId())).isNotNull();

    }

    @Test
    public void create1() throws Exception {
        Assertions.assertThat(findAllAdminFiltered(false)).isEmpty();
        User user = usecase.create("user1", "user_name", "password");
        Assertions.assertThat(findAllAdminFiltered(false))
            .hasSize(1)
            .first()
            .is(testFields("user1", "user_name", "", false))
            .is(testAllFieldMatch(user));
        Assertions.assertThat(user.getUserSettings()).isNotNull();
        Assertions.assertThat(usecase.findAvater(user.getId())).isNotNull();
    }

    @Test
    public void changePassword() throws Exception {
        User user = usecase.create("user1", "user_name", "password");
        usecase.changePassword(user, "password", "hogehoge");
        Assertions.assertThat(usecase.matchPassword(user, "hogehoge")).isTrue();

        //マッチしなければ例外
        Assertions.assertThatThrownBy(() -> usecase.changePassword(user, "password", "pass1"))
            .isInstanceOf(PasswordNotMatchException.class);
    }

    @Test
    public void save() throws Exception {
        usecase.create("user1", "user_name", "password");
        User user = usecase.findById("user1", true).get();
        UserSettings settings = user.getUserSettings();

        Assertions.assertThat(settings.isUseGravatar()).isFalse();

        user.changeName("hogera");
        user.changeEmail("hoge@hoge.com");
        user.setUserSettings(new UserSettings(true));

        User updated = usecase.save(user);
        User user2 = usecase.findById("user1", true).get();

        Assertions.assertThat(updated)
            .is(testAllFieldMatch(user2))
            .is(testFields("user1", "hogera", "hoge@hoge.com", false));

        Assertions.assertThat(updated.getUserSettings())
            .is(testSettingsFields(true));

        Assertions.assertThat(user2.getUserSettings())
            .is(testSettingsFields(true));

    }

    @Test
    public void findById() throws Exception {
        Assertions.assertThat(usecase.findById("admin"))
            .isPresent()
            .map(User::getUserSettings)
            .isNotPresent();

        Assertions.assertThat(usecase.findById("admin", true))
            .isPresent()
            .map(User::getUserSettings)
            .isPresent();

        Assertions.assertThat(usecase.findById("user2"))
            .isNotPresent()
            .map(User::getUserSettings)
            .isNotPresent();

        Assertions.assertThat(usecase.findById("user2", true))
            .isNotPresent()
            .map(User::getUserSettings)
            .isNotPresent();
    }

    @Test
    public void findAll() throws Exception {
        Assertions.assertThat(usecase.findAll(false)).hasSize(1);
    }

    @Test
    public void findAvater() throws Exception {
        Assertions.assertThat(usecase.findAvater("admin")).isNotNull();
    }

    @Test
    public void updateAvater() throws Exception {
        User user = usecase.findById("admin").get();
        usecase.updateAvater(user, new byte[]{});

        Assertions.assertThat(usecase.findAvater("admin")).hasSize(0);
    }

    @Test
    public void matchPassword() throws Exception {
        User user = usecase.create("hoge", "hoge", "hogera");
        Assertions.assertThat(usecase.matchPassword(user, "hogera")).isTrue();
        Assertions.assertThat(usecase.matchPassword(user, "hogerarara")).isFalse();
    }

}
