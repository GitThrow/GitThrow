package net.orekyuu.workbench.ticket.usecase;

import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.domain.model.TicketPriority;
import net.orekyuu.workbench.ticket.domain.model.TicketStatus;
import net.orekyuu.workbench.ticket.domain.model.TicketType;
import net.orekyuu.workbench.ticket.port.*;
import net.orekyuu.workbench.ticket.port.table.OpenTicketDao;
import net.orekyuu.workbench.ticket.port.table.TicketPriorityDao;
import net.orekyuu.workbench.ticket.port.table.TicketStatusDao;
import net.orekyuu.workbench.ticket.port.table.TicketTypeDao;
import net.orekyuu.workbench.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TicketUsecase {

    private final TicketRepository ticketRepository;
    private final TicketPriorityRepository priorityRepository;
    private final TicketStatusRepository statusRepository;
    private final TicketTypeRepository typeRepository;

    private final OpenTicketDao ticketDao;
    private final TicketPriorityDao priorityDao;
    private final TicketStatusDao statusDao;
    private final TicketTypeDao typeDao;

    private final TicketCommentRepository commentRepository;

    public TicketUsecase(TicketRepository ticketRepository, TicketPriorityRepository priorityRepository, TicketStatusRepository statusRepository, TicketTypeRepository typeRepository, OpenTicketDao ticketDao, TicketPriorityDao priorityDao, TicketStatusDao statusDao, TicketTypeDao typeDao, TicketCommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
        this.priorityRepository = priorityRepository;
        this.statusRepository = statusRepository;
        this.typeRepository = typeRepository;
        this.ticketDao = ticketDao;
        this.priorityDao = priorityDao;
        this.statusDao = statusDao;
        this.typeDao = typeDao;
        this.commentRepository = commentRepository;
    }


    @Transactional(readOnly = false)
    public Ticket create(Project project, String title, String desc, User assignee, User proponent, LocalDateTime limit, TicketType type, TicketStatus status, TicketPriority priority) {
        return ticketRepository.create(project, title, desc, assignee, proponent, limit, type, status, priority);
    }

    @Transactional(readOnly = false)
    public Ticket update(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findByProject(Project project) {
        return ticketRepository.findByProject(project);
    }

    public Optional<Ticket> findById(Project project, int num) {
        return ticketRepository.findById(project, num);
    }

    @Transactional(readOnly = false)
    public void deleteByProject(Project project) {
        commentRepository.deleteByProject(project);
        ticketRepository.deleteByProject(project);
    }

    @Transactional(readOnly = false)
    public void deleteAllByProject(Project project) {
        commentRepository.deleteByProject(project);

        ticketDao.deleteByProject(project.getId());
        statusDao.deleteByProject(project.getId());
        typeDao.deleteByProject(project.getId());
        priorityDao.deleteByProject(project.getId());
    }

    public List<TicketPriority> findPriorityByProject(Project project) {
        return priorityRepository.findByProject(project.getId());
    }

    public Optional<TicketPriority> findPriorityById(Project project, int id) {
        return priorityRepository.findById(project, id);
    }

    public List<TicketType> findTypeByProject(Project project) {
        return typeRepository.findByProject(project.getId());
    }

    public Optional<TicketType> findTypeById(Project project, int id) {
        return typeRepository.findById(project, id);
    }

    public List<TicketStatus> findStatusByProject(Project project) {
        return statusRepository.findByProject(project.getId());
    }

    public Optional<TicketStatus> findStatusById(Project project, int id) {
        return statusRepository.findById(project, id);
    }
}
