package net.orekyuu.gitthrow.controller.rest.project;

import net.orekyuu.gitthrow.controller.rest.RestApiTest;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestUsecase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PullRequestRestControllerTest extends RestApiTest {

    @Autowired
    private PullRequestUsecase pullRequestUsecase;

    @Override
    protected void onSetup() throws Exception {

    }

    @Test
    public void all() throws Exception {
        PullRequest request1 = pullRequestUsecase.create(project, "test1", "desc", user1, user1, "base", "target");
        PullRequest request2 = pullRequestUsecase.create(project, "test2", "desc", user1, user1, "base", "target");

        getMvc("/rest/project/pull-request", user1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].title", equalTo("test1")))
            .andDo(document);
    }

    @Test
    public void show() throws Exception {
        PullRequest request1 = pullRequestUsecase.create(project, "test1", "desc", user1, user1, "base", "target");
        PullRequest request2 = pullRequestUsecase.create(project, "test2", "desc", user1, user1, "base", "target");

        getMvc("/rest/project/pull-request/" + request1.getPullrequestNum(), user1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", equalTo("test1")))
            .andDo(document);

        getMvc("/rest/project/pull-request/" + request2.getPullrequestNum(), user1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", equalTo("test2")));

        getMvc("/rest/project/pull-request/" + 100, user1)
            .andExpect(status().isNotFound());
    }

    @Test
    public void create() throws Exception {
        Assertions.assertThat(pullRequestUsecase.findByProject(project)).isEmpty();

        postMvc("/rest/project/pull-request", "{\n" +
            "    \"project\" : \"project\",\n" +
            "    \"title\" : \"test1\",\n" +
            "    \"description\" : \"desc\",\n" +
            "    \"reviewer\" : {\n" +
            "        \"id\" : \"user1\"\n" +
            "    },\n" +
            "    \"base\" : \"base\",\n" +
            "    \"target\" : \"target\"\n" +
            "}", user1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", equalTo("test1")))
            .andExpect(jsonPath("$.proponent.id", equalTo("user1")))
            .andDo(document);

        Assertions.assertThat(pullRequestUsecase.findByProject(project))
            .hasSize(1)
            .first()
            .matches(pr -> pr.getTitle().equals("test1"));
    }

    @Test
    public void update() throws Exception {
        PullRequest pr = pullRequestUsecase.create(project, "hoge", "hoge", user1, user1, "base", "target");
        putMvc("/rest/project/pull-request/" + pr.getPullrequestNum(), "{\n" +
            "    \"title\" : \"fuga\",\n" +
            "    \"description\" : \"fuga\",\n" +
            "    \"reviewer\" : {\n" +
            "        \"id\" : \"user1\"\n" +
            "    },\n" +
            "    \"proponent\" : {\n" +
            "        \"id\" : \"user1\"\n" +
            "    },\n" +
            "    \"base\" : \"base2\",\n" +
            "    \"target\" : \"target2\",\n" +
            "    \"project\" : \"project\",\n" +
            "    \"pullrequestNum\" : 1,\n" +
            "    \"state\" : \"OPEN\"\n" +
            "}", user1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", equalTo("fuga")))
            .andDo(document);

        Assertions.assertThat(pullRequestUsecase.findById(project, pr.getPullrequestNum()).get())
            .matches(it -> it.getTitle().equals("fuga") && it.getDescription().equals("fuga") &&
                it.getBase().equals("base") && it.getTarget().equals("target")); //baseとtargetは変更できない
    }
}
