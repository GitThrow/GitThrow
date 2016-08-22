package net.orekyuu.workbench.service;

import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.entity.*;
import net.orekyuu.workbench.entity.dao.TicketPriorityDao;
import net.orekyuu.workbench.entity.dao.TicketStatusDao;
import net.orekyuu.workbench.entity.dao.TicketTypeDao;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.util.TestRepositoryUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TicketServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketStatusDao statusDao;
    @Autowired
    private TicketPriorityDao priorityDao;
    @Autowired
    private TicketTypeDao typeDao;

    private User user1;
    private User user2;
    private TicketStatus status1;
    private TicketPriority priority1;
    private TicketType type1;

    private TicketStatus status2;
    private TicketPriority priority2;
    private TicketType type2;

    @Before
    public void before() throws UserExistsException, ProjectExistsException {
        TestRepositoryUtil.deleteGitRepositoryDir();

        userService.createUser("user1", "user1", "pw");
        userService.createUser("user2", "user2", "pw");

        user1 = userService.findById("user1").get();
        user2 = userService.findById("user2").get();

        projectService.createProject("project1", "project1", user1);
        projectService.createProject("project2", "project2", user1);

        status1 = statusDao.findByProject("project1", Collectors.toList()).get(0);
        priority1 = priorityDao.findByProject("project1", Collectors.toList()).get(0);
        type1 = typeDao.findByProject("project1", Collectors.toList()).get(0);

        status2 = statusDao.findByProject("project2", Collectors.toList()).get(0);
        priority2 = priorityDao.findByProject("project2", Collectors.toList()).get(0);
        type2 = typeDao.findByProject("project2", Collectors.toList()).get(0);

    }

    private void createTicketToProject1(String title, String desc) {
        OpenTicket ticket = new OpenTicket();
        ticket.project = "project1";
        ticket.title = title;
        ticket.description = desc;
        ticket.assignee = user1.id;
        ticket.proponent = user1.id;
        ticket.type = type1.id;
        ticket.priority = priority1.id;
        ticket.status = status1.id;
        ticket.limit = LocalDateTime.now();
        ticketService.createTicket(ticket);
    }

    private void createTicketToProject2(String title, String desc) {
        OpenTicket ticket = new OpenTicket();
        ticket.project = "project2";
        ticket.title = title;
        ticket.description = desc;
        ticket.assignee = user1.id;
        ticket.proponent = user1.id;
        ticket.type = type2.id;
        ticket.priority = priority2.id;
        ticket.status = status2.id;
        ticket.limit = LocalDateTime.now();
        ticketService.createTicket(ticket);
    }

    @Test
    public void testCreate() throws ProjectExistsException {
        createTicketToProject1("タスク1", "テスト");
        createTicketToProject1("タスク2", "テスト");
    }

    @Test
    public void testCreateException() {
        Assertions.assertThatThrownBy(() -> {
            OpenTicket ticket = new OpenTicket();
            ticket.project = "project1";
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user1.id;
            ticket.proponent = user1.id;
            ticket.type = type2.id; //違うプロジェクトを参照してみる
            ticket.priority = priority1.id;
            ticket.status = status1.id;
            ticket.limit = LocalDateTime.now();
            ticketService.createTicket(ticket);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            OpenTicket ticket = new OpenTicket();
            ticket.project = "project1";
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user1.id;
            ticket.proponent = user1.id;
            ticket.type = type1.id;
            ticket.priority = priority2.id; //違うプロジェクトを参照してみる
            ticket.status = status1.id;
            ticket.limit = LocalDateTime.now();
            ticketService.createTicket(ticket);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            OpenTicket ticket = new OpenTicket();
            ticket.project = "project1";
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user1.id;
            ticket.proponent = user1.id;
            ticket.type = type1.id;
            ticket.priority = priority1.id;
            ticket.status = status2.id; //違うプロジェクトを参照してみる
            ticket.limit = LocalDateTime.now();
            ticketService.createTicket(ticket);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            OpenTicket ticket = new OpenTicket();
            ticket.project = "project1";
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user2.id;  //違うプロジェクトのユーザー
            ticket.proponent = user1.id;
            ticket.type = type1.id;
            ticket.priority = priority1.id;
            ticket.status = status1.id;
            ticket.limit = LocalDateTime.now();
            ticketService.createTicket(ticket);
        }).isInstanceOf(NotMemberException.class);

        Assertions.assertThatThrownBy(() -> {
            OpenTicket ticket = new OpenTicket();
            ticket.project = "project1";
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user1.id;
            ticket.proponent = user2.id; //違うプロジェクトのユーザー
            ticket.type = type1.id;
            ticket.priority = priority1.id;
            ticket.status = status1.id;
            ticket.limit = LocalDateTime.now();
            ticketService.createTicket(ticket);
        }).isInstanceOf(NotMemberException.class);
    }

    @Test
    public void testFindByProject() {
        createTicketToProject1("タスク1", "テスト");
        createTicketToProject1("タスク2", "テスト");
        createTicketToProject2("タスク3", "テスト");

        List<OpenTicket> project = ticketService.findOpenTicketByProject("project1");
        Assertions.assertThat(project).hasSize(2);
        Assertions.assertThat(project.get(0).title).isEqualTo("タスク1");
        Assertions.assertThat(project.get(0).ticketNum).isEqualTo(1);
        Assertions.assertThat(project.get(1).title).isEqualTo("タスク2");
        Assertions.assertThat(project.get(1).ticketNum).isEqualTo(2);
    }

    @Test
    public void testFindTicketModelByProject() {
        createTicketToProject1("タスク1", "テスト");
        createTicketToProject1("タスク2", "テスト");
        createTicketToProject2("タスク3", "テスト");

        List<TicketModel> project = ticketService.findOpenTicketModelByProject("project1");
        Assertions.assertThat(project).hasSize(2);
        Assertions.assertThat(project.get(0).getTitle()).isEqualTo("タスク1");
        Assertions.assertThat(project.get(0).getNumber()).isEqualTo(1);
        Assertions.assertThat(project.get(1).getTitle()).isEqualTo("タスク2");
        Assertions.assertThat(project.get(1).getNumber()).isEqualTo(2);
    }

    @Test
    public void testUpdate() {
        int newStatus = statusDao.findByProject("project1", Collectors.toList()).get(1).id;
        int newPriority = priorityDao.findByProject("project1", Collectors.toList()).get(1).id;
        int newType = typeDao.findByProject("project1", Collectors.toList()).get(1).id;

        //検証のためにプロジェクトに追加
        projectService.joinToProject("project1", user2.id);

        createTicketToProject1("hoge", "テスト");

        List<OpenTicket> ticketList = ticketService.findOpenTicketByProject("project1");

        OpenTicket ticket = ticketList.stream().filter(t -> t.title.equals("hoge")).findFirst().get();

        ticket.title = "fuga";
        ticket.description = "desc";
        ticket.limit = LocalDate.of(2016, 8, 22).atStartOfDay();
        ticket.assignee = user2.id;
        ticket.proponent = user2.id; //変更されない
        ticket.priority = newPriority;
        ticket.status = newStatus;
        ticket.type = newType;
        ticketService.update(ticket);

        OpenTicket updatedTicket = ticketService.findByProjectAndNum("project1", ticket.ticketNum).get();
        Assertions.assertThat(updatedTicket.title).isEqualTo("fuga");
        Assertions.assertThat(updatedTicket.description).isEqualTo("desc");
        Assertions.assertThat(updatedTicket.limit).isEqualTo(LocalDate.of(2016, 8, 22).atStartOfDay());
        Assertions.assertThat(updatedTicket.proponent).isEqualTo(user1.id); //変更されない
        Assertions.assertThat(updatedTicket.assignee).isEqualTo(user2.id);
        Assertions.assertThat(updatedTicket.priority).isEqualTo(newPriority);
        Assertions.assertThat(updatedTicket.status).isEqualTo(newStatus);
        Assertions.assertThat(updatedTicket.type).isEqualTo(newType);
    }

    @Test
    public void testUpdateException() {
        createTicketToProject1("hoge", "テスト");
        List<OpenTicket> ticketList = ticketService.findOpenTicketByProject("project1");
        OpenTicket ticket = ticketList.stream().filter(t -> t.title.equals("hoge")).findFirst().get();

        Assertions.assertThatThrownBy(() -> {
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user1.id;
            ticket.proponent = user1.id;
            ticket.type = type2.id; //違うプロジェクトを参照してみる
            ticket.priority = priority1.id;
            ticket.status = status1.id;
            ticket.limit = LocalDateTime.now();
            ticketService.update(ticket);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user1.id;
            ticket.proponent = user1.id;
            ticket.type = type1.id;
            ticket.priority = priority2.id; //違うプロジェクトを参照してみる
            ticket.status = status1.id;
            ticket.limit = LocalDateTime.now();
            ticketService.update(ticket);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user1.id;
            ticket.proponent = user1.id;
            ticket.type = type1.id;
            ticket.priority = priority1.id;
            ticket.status = status2.id; //違うプロジェクトを参照してみる
            ticket.limit = LocalDateTime.now();
            ticketService.update(ticket);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user2.id;  //違うプロジェクトのユーザー
            ticket.proponent = user1.id;
            ticket.type = type1.id;
            ticket.priority = priority1.id;
            ticket.status = status1.id;
            ticket.limit = LocalDateTime.now();
            ticketService.update(ticket);
        }).isInstanceOf(NotMemberException.class);

        Assertions.assertThatThrownBy(() -> {
            ticket.title = "タスク1";
            ticket.description = "";
            ticket.assignee = user1.id;
            ticket.proponent = user2.id; //違うプロジェクトのユーザー
            ticket.type = type1.id;
            ticket.priority = priority1.id;
            ticket.status = status1.id;
            ticket.limit = LocalDateTime.now();
            ticketService.update(ticket);
        }).isInstanceOf(NotMemberException.class);
    }

}
