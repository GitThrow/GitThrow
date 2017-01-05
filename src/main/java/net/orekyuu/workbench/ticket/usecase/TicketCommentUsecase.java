package net.orekyuu.workbench.ticket.usecase;

import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.domain.model.TicketComment;
import net.orekyuu.workbench.ticket.port.TicketCommentRepository;
import net.orekyuu.workbench.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TicketCommentUsecase {

    private final TicketCommentRepository commentRepository;

    public TicketCommentUsecase(TicketCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = false)
    public TicketComment create(Ticket ticket, String text, User user) {
        return commentRepository.create(ticket, text, user);
    }

    @Transactional(readOnly = false)
    public void delete(TicketComment comment) {
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = false)
    public void deleteByProject(Project project) {
        commentRepository.deleteByProject(project);
    }

    public List<TicketComment> findByTicket(Ticket ticket) {
        return commentRepository.findByTicket(ticket);
    }

}
