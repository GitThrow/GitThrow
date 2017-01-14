package net.orekyuu.gitthrow.controller.rest.project;

import net.orekyuu.gitthrow.controller.rest.RestApiTest;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequestComment;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestCommentUsecase;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestUsecase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class PullRequestCommentRestControllerTest extends RestApiTest {

    @Autowired
    private PullRequestUsecase pullRequestUsecase;

    @Autowired
    private PullRequestCommentUsecase commentUsecase;
    private PullRequest pr;

    @Override
    protected void onSetup() throws Exception {
        pr = pullRequestUsecase.create(project, "test1", "test1", user1, user1, "base", "target");
    }

    @Test
    public void testAll() throws Exception {
        PullRequestComment comment1 = commentUsecase.create(pr, "comment1", user1);
        PullRequestComment comment2 = commentUsecase.create(pr, "comment2", user1);

        getMvc("/rest/project/pull-request/" +pr.getPullrequestNum()+ "/comment", user1)
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$.[0].text", equalTo("comment1")))
            .andDo(document);
    }

    @Test
    public void testCreate() throws Exception {
        Assertions.assertThat(commentUsecase.findByPullRequest(pr)).hasSize(0);

        postMvc("/rest/project/pull-request/" +pr.getPullrequestNum()+ "/comment",
            "{\n" +
            "    \"text\" : \"comment1\",\n" +
            "    \"user\" : {\n" +
            "        \"id\" : \"user1\"\n" +
            "    }\n" +
            "}", user1)
            .andExpect(jsonPath("$.text", equalTo("comment1")))
            .andExpect(jsonPath("$.user.id", equalTo("user1")))
            .andDo(document);

        Assertions.assertThat(commentUsecase.findByPullRequest(pr)).hasSize(1).first()
            .matches(it -> it.getText().equals("comment1"))
            .matches(it -> it.getUser().getId().equals("user1"));
    }
}
