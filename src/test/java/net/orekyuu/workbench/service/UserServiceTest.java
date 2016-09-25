package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.util.TestRepositoryUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TestRepositoryUtil testRepositoryUtil;

    @Before
    public void before() {
        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();
    }

    @Test
    public void testCreate() throws UserExistsException {
        {
            Optional<User> user1 = userService.findById("user1");
            Assertions.assertThat(user1).isEmpty();
        }

        userService.createUser("user1", "user1", "pw");

        {
            Optional<User> user1 = userService.findById("user1");
            Assertions.assertThat(user1).isNotEmpty();
        }
    }

    @Test
    public void createBotUser() {
        Assertions.assertThatThrownBy(() -> userService.createUser("hoge_bot", "hoge_bot", "pw"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test(expected = UserExistsException.class)
    public void testUserExistsException() throws UserExistsException {
        try {
            userService.createUser("user1", "user1", "pw");
        } catch (UserExistsException e) {
            Assertions.fail("ここで落ちるのはおかしい");
        }
        userService.createUser("user1", "user1", "pw");
    }

    @Test
    public void testFindAll() throws UserExistsException {
        userService.createUser("user1", "user1", "pw");
        userService.createUser("user2", "user2", "pw");
        userService.createBot("test");

        Assertions.assertThat(userService.findAll(true)).hasSize(4); //adminが自動作成されるので+1
        Assertions.assertThat(userService.findAll(false)).hasSize(3);
    }

    @Test
    public void testCreateBot() throws UserExistsException {
        userService.createBot("aaaaaaaaaaaaaaaa");//16文字(プロジェクトidの最大の長さ)
        Assertions.assertThat(userService.findById("aaaaaaaaaaaaaaaa_bot")).isNotEmpty();

        userService.createBot("a");//1文字
        Assertions.assertThat(userService.findById("a_bot")).isNotEmpty();

        userService.createBot("abc");
        Assertions.assertThat(userService.findById("abc_bot")).isNotEmpty();

        Assertions.assertThat(userService.findById("hoge_bot")).isEmpty();

        Assertions.assertThatThrownBy(() -> userService.createBot("abc")).isInstanceOf(UserExistsException.class);
    }
}
