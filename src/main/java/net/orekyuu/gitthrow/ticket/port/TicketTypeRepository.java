package net.orekyuu.gitthrow.ticket.port;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.ticket.domain.model.TicketType;
import net.orekyuu.gitthrow.ticket.port.table.TicketTypeDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class TicketTypeRepository {
    private final TicketTypeDao ticketTypeDao;

    public TicketTypeRepository(TicketTypeDao ticketTypeDao) {
        this.ticketTypeDao = ticketTypeDao;
    }

    public Optional<TicketType> findById(Project project, int id) {
        return ticketTypeDao.findById(id)
            .filter(table -> table.getProject().equals(project.getId()))
            .map(it -> new TicketType(it.getId().intValue(), it.getType()));
    }

    public List<TicketType> findByProject(String id) {
        return ticketTypeDao.findByProject(id, Collectors.toList()).stream()
            .map(it -> new TicketType(it.getId().intValue(), it.getType()))
            .collect(Collectors.toList());
    }
}
