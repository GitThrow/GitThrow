package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import org.assertj.core.api.Assertions;
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

    @Test(expected = UserExistsException.class)
    public void testUserExistsException() throws UserExistsException {
        try {
            userService.createUser("user1", "user1", "pw");
        } catch (UserExistsException e) {
            Assertions.fail("ここで落ちるのはおかしい");
        }
        userService.createUser("user1", "user1", "pw");
    }
}
