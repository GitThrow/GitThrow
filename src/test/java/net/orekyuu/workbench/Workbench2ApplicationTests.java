package net.orekyuu.workbench;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.UserService;
import net.orekyuu.workbench.util.TestRepositoryUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class Workbench2ApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private TestRepositoryUtil testRepositoryUtil;

    @Before
    public void before() {
        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();
    }

	@Test
	public void contextLoads() {
	}

	//管理者ユーザーが作られているか
	@Test
    public void createAdminUser() {
        Optional<User> admin = userService.findById("admin");
        Assertions.assertThat(admin).isNotEmpty();

        User user = admin.get();
        Assertions.assertThat(user.id).isEqualTo("admin");
        Assertions.assertThat(user.name).isEqualTo("admin");
        Assertions.assertThat(user.admin).isTrue();
        Assertions.assertThat(user.password).matches(pw -> passwordEncoder.matches("password", pw));
    }

}
