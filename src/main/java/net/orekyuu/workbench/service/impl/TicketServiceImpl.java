package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.OpenTicket;
import net.orekyuu.workbench.entity.TicketPriority;
import net.orekyuu.workbench.entity.TicketStatus;
import net.orekyuu.workbench.entity.TicketType;
import net.orekyuu.workbench.entity.dao.*;
import net.orekyuu.workbench.service.TicketModel;
import net.orekyuu.workbench.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

    @Autowired
    public OpenTicketDao openTicketDao;
    @Autowired
    private TicketNumDao ticketNumDao;

    @Autowired
    private TicketStatusDao statusDao;
    @Autowired
    private TicketPriorityDao priorityDao;
    @Autowired
    private TicketTypeDao typeDao;

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

    @Override
    public List<TicketModel> findOpenTicketModelByProject(String projectId) {
        Map<Integer, String> statusMap = statusDao.findByProject(projectId,
            Collectors.toMap((TicketStatus k) -> k.id, (TicketStatus v) -> v.status));
        Map<Integer, String> priorityMap = priorityDao.findByProject(projectId,
            Collectors.toMap((TicketPriority k) -> k.id, (TicketPriority v) -> v.priority));
        Map<Integer, String> typeMap = typeDao.findByProject(projectId,
            Collectors.toMap((TicketType k) -> k.id, (TicketType v) -> v.type));

        List<OpenTicket> openTicketList = openTicketDao.findByProject(projectId, Collectors.toList());

        List<TicketModel> modelList = openTicketList.stream().map(t -> new TicketModel(
            t.ticketNum,
            t.title,
            typeMap.get(t.type),
            priorityMap.get(t.priority),
            statusMap.get(t.status))
        ).collect(Collectors.toList());

        return modelList;
    }
}
