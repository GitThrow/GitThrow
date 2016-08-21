package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.TicketStatus;
import net.orekyuu.workbench.entity.dao.TicketStatusDao;
import net.orekyuu.workbench.service.TicketStatusService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class TicketStatusServiceImpl implements TicketStatusService {

    @Autowired
    private TicketStatusDao statusDao;

    @Override
    public List<TicketStatus> findByProject(String projectId) {
        return statusDao.findByProject(projectId, Collectors.toList());
    }
}
