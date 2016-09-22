package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.rest.model.TicketCommentModel;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rest/ticket/comment")
public class TicketCommentRestController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<TicketCommentModel> show(@RequestParam("project") String projectId,
                                         @RequestParam("id") int id,
                                         @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(projectId, principal.getUser().id)) {
            throw new NotMemberException();
        }
        return Arrays.asList(
            new TicketCommentModel("admin", "mock", "### mockmock\n- mock\n- mock\n"),
            new TicketCommentModel("orekyuu", "orekyuu", "piyo"),
            new TicketCommentModel("admin", "hoge", "fuga")
        );
    }
}
