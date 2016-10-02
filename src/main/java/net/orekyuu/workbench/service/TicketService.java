package net.orekyuu.workbench.service;

import net.orekyuu.workbench.controller.rest.model.TicketModel;
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

    /**
     * チケットを更新する
     * @param ticket チケット
     */
    void update(OpenTicket ticket);

    Optional<OpenTicket> findByProjectAndNum(String projectId, int number);

    /**
     * プロジェクトにある開いているチケットを検索します
     * @param projectId プロジェクトID
     * @return プロジェクトにある開いているチケット
     */
    List<OpenTicket> findOpenTicketByProject(String projectId);

    List<TicketModel> findOpenTicketModelByProject(String projectId);

    /**
     * 指定のプロジェクト内のチケットから担当者で検索します
     * @param projectId プロジェクトID
     * @param assignee 担当者
     * @return 担当者にマッチするチケット
     */
    List<OpenTicket> findTicketByProjectAndAssignee(String projectId, String assignee);
}
