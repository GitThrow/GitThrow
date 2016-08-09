package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.OpenTicket;
import net.orekyuu.workbench.entity.dao.OpenTicketDao;
import net.orekyuu.workbench.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Component
public class TicketServiceImpl implements TicketService {

    @Autowired
    public OpenTicketDao openTicketDao;

    @Transactional(readOnly = false)
    @Override
    public void createTicket(OpenTicket ticket) {
    }

    @Override
    public Optional<OpenTicket> findByProjectAndNum(String projectId, int number) {
        return null;
    }
}
