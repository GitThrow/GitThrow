package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.controller.rest.model.TicketModel;
import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.entity.OpenTicket;
import net.orekyuu.workbench.entity.TicketPriority;
import net.orekyuu.workbench.entity.TicketStatus;
import net.orekyuu.workbench.entity.TicketType;
import net.orekyuu.workbench.entity.dao.*;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class TicketServiceImpl implements TicketService {

    @Autowired
    public OpenTicketDao openTicketDao;
    @Autowired
    private TicketNumDao ticketNumDao;

    @Autowired
    private TicketStatusDao statusDao;
    @Autowired
    private TicketPriorityDao priorityDao;
    @Autowired
    private TicketTypeDao typeDao;
    @Autowired
    private ProjectService projectService;

    private Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Transactional(readOnly = false)
    @Override
    public void createTicket(OpenTicket ticket) {
        check(ticket);
        ticketNumDao.increment(ticket.project);
        openTicketDao.insert(ticket);
    }

    @Transactional(readOnly = false)
    @Override
    public void update(OpenTicket ticket) {
        check(ticket);
        openTicketDao.update(ticket);
    }

    @Override
    public Optional<OpenTicket> findByProjectAndNum(String projectId, int number) {
        return openTicketDao.findByProjectAndNum(projectId, number);
    }

    @Override
    public List<OpenTicket> findOpenTicketByProject(String projectId) {
        return openTicketDao.findByProject(projectId, Collectors.toList());
    }

    @Override
    public List<TicketModel> findOpenTicketModelByProject(String projectId) {
        Map<Integer, String> statusMap = statusDao.findByProject(projectId,
            Collectors.toMap((TicketStatus k) -> k.id, (TicketStatus v) -> v.status));
        Map<Integer, String> priorityMap = priorityDao.findByProject(projectId,
            Collectors.toMap((TicketPriority k) -> k.id, (TicketPriority v) -> v.priority));
        Map<Integer, String> typeMap = typeDao.findByProject(projectId,
            Collectors.toMap((TicketType k) -> k.id, (TicketType v) -> v.type));

        List<OpenTicket> openTicketList = openTicketDao.findByProject(projectId, Collectors.toList());

        List<TicketModel> modelList = openTicketList.stream().map(t -> new TicketModel(
            t.ticketNum,
            t.title,
            t.description,
            typeMap.get(t.type),
            priorityMap.get(t.priority),
            statusMap.get(t.status),
            t.limit,
            t.assignee,
            t.proponent
            )
        ).collect(Collectors.toList());

        return modelList;
    }

    @Override
    public List<OpenTicket> findTicketByProjectAndAssignee(String projectId, String assignee) {
        return openTicketDao.findByAssignee(projectId, assignee, Collectors.toList());
    }

    private void check(OpenTicket ticket) {
        if (!typeDao.findById(ticket.type).map(t -> t.project.equals(ticket.project)).orElse(false)) {
            logger.warn(String.format("不正なTicketTypeのID: type_id=%d, project_id=%s", ticket.type, ticket.project));
            throw new IllegalArgumentException("プロジェクトに存在しないtype_id");
        }

        if (!statusDao.findById(ticket.status).map(t -> t.project.equals(ticket.project)).orElse(false)) {
            logger.warn(String.format("不正なTicketStatusのID: status_id=%d, project_id=%s", ticket.status, ticket.project));
            throw new IllegalArgumentException("プロジェクトに存在しないstatus_id");
        }

        if (!priorityDao.findById(ticket.priority).map(t -> t.project.equals(ticket.project)).orElse(false)) {
            logger.warn(String.format("不正なTicketPriorityのID: priority_id=%d, project_id=%s", ticket.priority, ticket.project));
            throw new IllegalArgumentException("プロジェクトに存在しないpriority_id");
        }

        //担当者がプロジェクトメンバーか
        if (!projectService.isJoined(ticket.project, ticket.assignee)) {
            throw new NotMemberException();
        }
        //作成者がプロジェクトメンバーか
        if (!projectService.isJoined(ticket.project, ticket.proponent)) {
            throw new NotMemberException();
        }
    }
}
