package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.TicketPriority;
import net.orekyuu.workbench.entity.dao.TicketPriorityDao;
import net.orekyuu.workbench.service.TicketPriorityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class TicketPriorityServiceImpl implements TicketPriorityService {

    @Autowired
    private TicketPriorityDao priorityDao;

    @Override
    public List<TicketPriority> findByProject(String projectId) {
        return priorityDao.findByProject(projectId, Collectors.toList());
    }
}
