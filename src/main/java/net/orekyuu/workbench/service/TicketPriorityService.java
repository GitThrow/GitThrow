package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.TicketPriority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TicketPriorityService {

    List<TicketPriority> findByProject(String projectId);

    Optional<TicketPriority> findById(int id);
}
