package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.OpenTicket;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TicketService {

    /**
     * チケットを作成する
     * @param ticket チケット
     */
    void createTicket(OpenTicket ticket);

    Optional<OpenTicket> findByProjectAndNum(String projectId, int number);

    /**
     * プロジェクトにある開いているチケットを検索します
     * @param projectId プロジェクトID
     * @return プロジェクトにある開いているチケット
     */
    List<OpenTicket> findOpenTicketByProject(String projectId);

    List<TicketModel> findOpenTicketModelByProject(String projectId);
}
