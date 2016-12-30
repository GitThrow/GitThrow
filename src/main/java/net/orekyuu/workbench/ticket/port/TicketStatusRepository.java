package net.orekyuu.workbench.ticket.port;

import net.orekyuu.workbench.ticket.domain.model.TicketStatus;
import net.orekyuu.workbench.ticket.port.table.TicketStatusDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class TicketStatusRepository {

    private final TicketStatusDao statusDao;

    public TicketStatusRepository(TicketStatusDao statusDao) {
        this.statusDao = statusDao;
    }

    public Optional<TicketStatus> findById(int id) {
        return statusDao.findById(id).map(it -> new TicketStatus(it.getId().intValue(), it.getStatus()));
    }

    public List<TicketStatus> findByProject(String id) {
        return statusDao.findByProject(id, Collectors.toList()).stream()
            .map(it -> new TicketStatus(it.getId().intValue(), it.getStatus()))
            .collect(Collectors.toList());
    }
}
