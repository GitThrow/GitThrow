package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.TicketStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TicketStatusService {

    List<TicketStatus> findByProject(String projectId);

    Optional<TicketStatus> findById(int id);
}
