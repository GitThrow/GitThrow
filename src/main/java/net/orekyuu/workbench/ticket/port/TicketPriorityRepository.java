package net.orekyuu.workbench.ticket.port;

import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.ticket.domain.model.TicketPriority;
import net.orekyuu.workbench.ticket.port.table.TicketPriorityDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class TicketPriorityRepository {

    private final TicketPriorityDao priorityDao;

    public TicketPriorityRepository(TicketPriorityDao priorityDao) {
        this.priorityDao = priorityDao;
    }

    public Optional<TicketPriority> findById(Project project, int id) {
        return priorityDao.findById(id)
            .filter(table -> table.getProject().equals(project.getId()))
            .map(it -> new TicketPriority(it.getId().intValue(), it.getPriority()));
    }

    public List<TicketPriority> findByProject(String projectId) {
        return priorityDao.findByProject(projectId, Collectors.toList()).stream()
            .map(it -> new TicketPriority(it.getId().intValue(), it.getPriority()))
            .collect(Collectors.toList());
    }
}
