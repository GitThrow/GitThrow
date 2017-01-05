package net.orekyuu.workbench.ticket.port;

import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.domain.model.TicketComment;
import net.orekyuu.workbench.ticket.port.table.TicketCommentDao;
import net.orekyuu.workbench.ticket.port.table.TicketCommentTable;
import net.orekyuu.workbench.ticket.util.TicketUtil;
import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.usecase.UserUsecase;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TicketCommentRepository {

    private final TicketCommentDao commentDao;
    private final UserUsecase userUsecase;

    public TicketCommentRepository(TicketCommentDao commentDao, UserUsecase userUsecase) {
        this.commentDao = commentDao;
        this.userUsecase = userUsecase;
    }

    public TicketComment create(Ticket ticket, String text, User user) {
        TicketCommentTable table = new TicketCommentTable(null, ticket.getProjectId(), (long)ticket.getTicketNum(),
            text, LocalDateTime.now(), user.getId());

        TicketCommentTable result = commentDao.insert(table).getEntity();
        userUsecase.findById(result.getUserId());

        return TicketUtil.commentFromTable(result, userUsecase.findById(result.getUserId()).orElse(null));
    }

    public List<TicketComment> findByTicket(Ticket ticket) {
        HashMap<String, User> userMap = new HashMap<>();

        return commentDao.findByProjectAndTicketNum(ticket.getProjectId(), ticket.getTicketNum(),
            Collectors.mapping(it -> TicketUtil.commentFromTable(
                it,
                userMap.computeIfAbsent(it.getUserId(), userId -> userUsecase.findById(userId).orElse(null))
                ),
                Collectors.toList())
        );
    }

    public void delete(TicketComment comment) {
        commentDao.delete(new TicketCommentTable((long)comment.getId(), null, null, null, null, null));
    }

    public void deleteByProject(Project project) {
        commentDao.deleteByProject(project.getId());
    }

}
