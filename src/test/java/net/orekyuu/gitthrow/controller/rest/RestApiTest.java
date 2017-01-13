package net.orekyuu.gitthrow.controller.rest;

import net.orekyuu.gitthrow.config.security.WorkbenchUserDetails;
import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import net.orekyuu.gitthrow.util.ProjectTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public abstract class RestApiTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private Filter springSecurityFilterChain;
    @Rule
    public JUnitRestDocumentation restDocumentation =
        new JUnitRestDocumentation("target/generated-snippets");
    private MockMvc mvc;

    @Autowired
    private ProjectTestUtil util;

    @Autowired
    protected UserUsecase userUsecase;
    @Autowired
    protected ProjectUsecase projectUsecase;

    protected User user1;
    protected User user2;
    protected Project project;

    protected RestDocumentationResultHandler document;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(documentationConfiguration(this.restDocumentation))
            .addFilters(springSecurityFilterChain)
            .build();

        this.document = document("{class-name}/{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));

        user1 = userUsecase.create("user1", "user1", "password");
        user2 = userUsecase.create("user2", "user2", "password");
        project = projectUsecase.createProject("project", "project", user1);

        onSetup();
    }

    protected void onSetup() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        util.deleteGitRepositoryAndWorkspaceDir();
        onTearDown();
    }

    protected void onTearDown() throws Exception {
    }

    protected final ResultActions postMvc(String url, String content, User user) throws Exception {
        return mvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .with(user(new WorkbenchUserDetails(user, "password")))
            .with(csrf().asHeader()));
    }

    protected final ResultActions putMvc(String url, String content, User user) throws Exception{
        return mvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .with(user(new WorkbenchUserDetails(user, "password")))
            .with(csrf().asHeader()));
    }

    protected final ResultActions getMvc(String url, User user) throws Exception {
        return mvc.perform(get(url).with(user(new WorkbenchUserDetails(user, "password"))));
    }
}
