package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.TicketStatus;
import net.orekyuu.workbench.entity.dao.TicketStatusDao;
import net.orekyuu.workbench.service.TicketStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class TicketStatusServiceImpl implements TicketStatusService {

    @Autowired
    private TicketStatusDao statusDao;

    @Override
    public List<TicketStatus> findByProject(String projectId) {
        return statusDao.findByProject(projectId, Collectors.toList());
    }

    @Override
    public Optional<TicketStatus> findById(int id) {
        return statusDao.findById(id);
    }
}
