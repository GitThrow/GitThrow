package net.orekyuu.gitthrow.ticket.port;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.ticket.domain.model.Ticket;
import net.orekyuu.gitthrow.ticket.domain.model.TicketPriority;
import net.orekyuu.gitthrow.ticket.domain.model.TicketStatus;
import net.orekyuu.gitthrow.ticket.domain.model.TicketType;
import net.orekyuu.gitthrow.ticket.port.table.*;
import net.orekyuu.gitthrow.ticket.util.TicketUtil;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.port.UserRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TicketRepository {

    private final OpenTicketDao ticketDao;

    private final TicketTypeDao typeDao;
    private final TicketStatusDao statusDao;
    private final TicketPriorityDao priorityDao;
    private final TicketNumDao ticketNumDao;

    private final UserRepository userRepository;
    private final ProjectUsecase projectUsecase;

    public TicketRepository(OpenTicketDao ticketDao, TicketTypeDao typeDao, TicketStatusDao statusDao, TicketPriorityDao priorityDao, TicketNumDao ticketNumDao, UserRepository userRepository, ProjectUsecase projectUsecase) {
        this.ticketDao = ticketDao;
        this.typeDao = typeDao;
        this.statusDao = statusDao;
        this.priorityDao = priorityDao;
        this.ticketNumDao = ticketNumDao;
        this.userRepository = userRepository;
        this.projectUsecase = projectUsecase;
    }

    /**
     * チケットを新規作成する
     *
     * @param project   プロジェクト
     * @param title     タイトル
     * @param desc      説明文
     * @param assignee  担当者
     * @param proponent 提案者
     * @param limit     期日
     * @param type      タイプ
     * @param status    ステータス
     * @param priority  優先度
     * @return 作成したチケット
     */
    public Ticket create(Project project, String title, String desc, User assignee, User proponent, LocalDateTime limit, TicketType type, TicketStatus status, TicketPriority priority) {
        List<String> error = new ArrayList<>();

        TicketTypeTable typeTable = typeDao.findById(type.getId()).get();
        TicketStatusTable statusTable = statusDao.findById(status.getId()).get();
        TicketPriorityTable priorityTable = priorityDao.findById(priority.getId()).get();
        if (!typeTable.getProject().equals(project.getId())) {
            error.add("ticketType");
        }
        if (!statusTable.getProject().equals(project.getId())) {
            error.add("statusTable");
        }
        if (!priorityTable.getProject().equals(project.getId())) {
            error.add("priorityTable");
        }

        if (assignee != null && !projectUsecase.isJoined(project.getId(), assignee.getId())) {
            error.add("assignee");
        }

        if (!projectUsecase.isJoined(project.getId(), proponent.getId())) {
            error.add("proponent");
        }

        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error.stream().collect(Collectors.joining(", ")) + "がprojectIdと一致しません");
        }

        TicketNumTable numTable = ticketNumDao.findByProjectID(project.getId()).get();
        numTable = ticketNumDao.update(new TicketNumTable(project.getId(), numTable.getTicketCount() + 1L)).getEntity();
        OpenTicketTable ticketTable = ticketDao.insert(new OpenTicketTable(
            project.getId(),
            numTable.getTicketCount().intValue(),
            title,
            desc,
            assignee.getId(),
            proponent.getId(),
            limit,
            type.getId(),
            status.getId(),
            priority.getId())).getEntity();


        return TicketUtil.fromTable(
            ticketTable,
            typeTable,
            statusTable,
            priorityTable,
            assignee,
            proponent
        );
    }

    /**
     * チケットを更新する
     *
     * @param ticket 更新するチケット
     * @return 更新後のチケット
     */
    public Ticket save(Ticket ticket) {
        List<String> error = new ArrayList<>();

        TicketTypeTable typeTable = typeDao.findById(ticket.getType().getId()).get();
        TicketStatusTable statusTable = statusDao.findById(ticket.getStatus().getId()).get();
        TicketPriorityTable priorityTable = priorityDao.findById(ticket.getPriority().getId()).get();
        if (!typeTable.getProject().equals(ticket.getProjectId())) {
            error.add("ticketType");
        }
        if (!statusTable.getProject().equals(ticket.getProjectId())) {
            error.add("statusTable");
        }
        if (!priorityTable.getProject().equals(ticket.getProjectId())) {
            error.add("priorityTable");
        }

        User assignee = null;
        if (ticket.getAssignee() != null) {
            assignee = userRepository.findById(ticket.getAssignee().getId()).get();
            if (!projectUsecase.isJoined(ticket.getProjectId(), assignee.getId())) {
                error.add("assignee");
            }
        }

        User proponent = null;
        if (ticket.getProponent() != null) {
            proponent = userRepository.findById(ticket.getProponent().getId()).get();
            if (!projectUsecase.isJoined(ticket.getProjectId(), proponent.getId())) {
                error.add("proponent");
            }
        }

        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error.stream().collect(Collectors.joining(", ")) + "がprojectIdと一致しません");
        }


        OpenTicketTable result = ticketDao.update(new OpenTicketTable(
            ticket.getProjectId(),
            ticket.getTicketNum(),
            ticket.getTitle(),
            ticket.getDescription(),
            ticket.getAssignee() == null ? null : ticket.getAssignee().getId(),
            ticket.getProponent() == null ? null : ticket.getProponent().getId(),
            ticket.getLimit(),
            ticket.getType().getId(),
            ticket.getStatus().getId(),
            ticket.getPriority().getId()
        )).getEntity();

        return TicketUtil.fromTable(
            result,
            typeTable,
            statusTable,
            priorityTable,
            assignee,
            proponent
        );
    }

    /**
     * プロジェクト内のチケットをすべて削除する
     *
     * @param project 削除対象のプロジェクト
     */
    public void deleteByProject(Project project) {
        ticketDao.deleteByProject(project.getId());
    }

    /**
     * プロジェクト内のチケットをすべて取得する
     *
     * @param project 対象のプロジェクト
     * @return プロジェクト内のチケットすべて
     */
    public List<Ticket> findByProject(Project project) {
        Map<Long, TicketStatusTable> statusMap = statusDao.findByProject(project.getId(),
            Collectors.toMap(TicketStatusTable::getId, it -> it));
        Map<Long, TicketTypeTable> typeMap = typeDao.findByProject(project.getId(),
            Collectors.toMap(TicketTypeTable::getId, it -> it));
        Map<Long, TicketPriorityTable> priorityMap = priorityDao.findByProject(project.getId(),
            Collectors.toMap(TicketPriorityTable::getId, it -> it));

        return ticketDao.findByProject(project.getId(),
            Collectors.mapping(it -> TicketUtil.fromTable(
                it,
                typeMap.get((long) it.getType()),
                statusMap.get((long) it.getStatus()),
                priorityMap.get((long) it.getPriority())
            ), Collectors.toList()));
    }

    /**
     * チケットを一件取得する
     *
     * @param project プロジェクト
     * @param id      チケット番号
     * @return 見つかったチケット
     */
    public Optional<Ticket> findById(Project project, int id) {
        return ticketDao.findByProjectAndNum(project.getId(), id)
            .map(it -> TicketUtil.fromTable(
                it,
                typeDao.findById(it.getType()).get(),
                statusDao.findById(it.getStatus()).get(),
                priorityDao.findById(it.getPriority()).get(),
                userRepository.findById(it.getAssignee()).get(),
                userRepository.findById(it.getProponent()).get()
            ));
    }
}
