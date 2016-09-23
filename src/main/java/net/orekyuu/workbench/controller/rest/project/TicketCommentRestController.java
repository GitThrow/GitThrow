package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.config.security.WorkbenchUserDetails;
import net.orekyuu.workbench.controller.rest.model.TicketCommentModel;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.TicketCommentService;
import net.orekyuu.workbench.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/ticket/comment")
public class TicketCommentRestController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private TicketCommentService commentService;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<TicketCommentModel> show(@RequestParam("project") String projectId,
                                         @RequestParam("id") int id,
                                         @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(projectId, principal.getUser().id)) {
            throw new NotMemberException();
        }

        Map<String, User> cache = new TreeMap<>();
        return commentService.findByTicket(projectId, id).stream()
            .map(comment -> {
                User user = findUser(cache, comment.userId);
                return new TicketCommentModel(user.id, user.name, comment.text, comment.createdAt);
            })
            .collect(Collectors.toList());
    }

    private User findUser(Map<String, User> cache, String id) {
        return cache.computeIfAbsent(id, key -> userService.findById(key).orElseThrow(() -> new RuntimeException(key)));
    }

    @PostMapping
    public void create(@RequestBody CommentCreateRequest req,
                       @AuthenticationPrincipal WorkbenchUserDetails principal) {
        if (!projectService.isJoined(req.getProject(), principal.getUser().id)) {
            throw new NotMemberException();
        }

        commentService.createComment(req.getProject(), req.getId(), req.getText(), principal.getUser().id);
    }
}
