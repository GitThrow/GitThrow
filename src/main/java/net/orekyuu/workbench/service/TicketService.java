package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.OpenTicket;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TicketService {

    void createTicket(OpenTicket ticket);

    Optional<OpenTicket> findByProjectAndNum(String projectId, int number);

    List<OpenTicket> findOpenTicketByProject(String projectId);
}
