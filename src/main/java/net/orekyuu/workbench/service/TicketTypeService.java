package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.TicketType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TicketTypeService {

    List<TicketType> findByProject(String projectId);

    Optional<TicketType> findById(int id);
}
