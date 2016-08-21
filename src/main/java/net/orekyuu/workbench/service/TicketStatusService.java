package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.TicketStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketStatusService {

    List<TicketStatus> findByProject(String projectId);
}
