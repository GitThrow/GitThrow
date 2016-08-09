package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.OpenTicket;
import net.orekyuu.workbench.entity.dao.OpenTicketDao;
import net.orekyuu.workbench.entity.dao.TicketNumDao;
import net.orekyuu.workbench.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Component
public class TicketServiceImpl implements TicketService {

    @Autowired
    public OpenTicketDao openTicketDao;
    @Autowired
    private TicketNumDao ticketNumDao;

    @Transactional(readOnly = false)
    @Override
    public void createTicket(OpenTicket ticket) {
        ticketNumDao.increment(ticket.project);
        openTicketDao.insert(ticket);
    }

    @Override
    public Optional<OpenTicket> findByProjectAndNum(String projectId, int number) {
        return null;
    }

    @Override
    public List<OpenTicket> findOpenTicketByProject(String projectId) {
        return openTicketDao.findByProject(projectId, Collectors.toList());
    }
}
