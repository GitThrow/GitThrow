package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.TicketType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketTypeService {

    List<TicketType> findByProject(String projectId);
}
