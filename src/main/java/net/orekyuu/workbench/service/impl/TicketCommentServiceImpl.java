package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.TicketComment;
import net.orekyuu.workbench.entity.dao.TicketCommentDao;
import net.orekyuu.workbench.service.TicketCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class TicketCommentServiceImpl implements TicketCommentService {

    @Autowired
    private TicketCommentDao ticketCommentDao;

    @Transactional(readOnly = false)
    @Override
    public void createComment(String projectId, int ticketNum, String text, String userId) {
        TicketComment comment = new TicketComment(projectId, ticketNum, text, LocalDateTime.now(), userId);
        ticketCommentDao.insert(comment);
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteByProject(String projectId) {
        ticketCommentDao.deleteByProject(projectId);
    }

    @Override
    public List<TicketComment> findByTicket(String projectId, int ticketNum) {
        return ticketCommentDao.findByProjectAndTicketNum(projectId, ticketNum, Collectors.toList());
    }
}
