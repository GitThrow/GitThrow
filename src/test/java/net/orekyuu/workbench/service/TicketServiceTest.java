package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.*;
import net.orekyuu.workbench.entity.dao.TicketNumDao;
import net.orekyuu.workbench.entity.dao.TicketPriorityDao;
import net.orekyuu.workbench.entity.dao.TicketStatusDao;
import net.orekyuu.workbench.entity.dao.TicketTypeDao;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    private TicketNumDao ticketNumDao;
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

    @Before
    public void before() throws UserExistsException, ProjectExistsException {
        userService.createUser("user1", "user1", "pw");
        userService.createUser("user2", "user2", "pw");

        user1 = userService.findById("user1").get();
        user2 = userService.findById("user2").get();

        projectService.createProject("project1", "project1", user1);
        projectService.createProject("project2", "project2", user1);

        status1 = statusDao.findByProject("project1", Collectors.toList()).get(0);
        priority1 = priorityDao.findByProject("project1", Collectors.toList()).get(0);
        type1 = typeDao.findByProject("project1", Collectors.toList()).get(0);

    }

    private void createTicket(String project, String title, String desc) {
        OpenTicket ticket = new OpenTicket();
        ticket.project = project;
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

    @Test
    public void testCreate() throws ProjectExistsException {
        createTicket("project1", "タスク1", "テスト");
        createTicket("project1", "タスク2", "テスト");
    }

    @Test
    public void testFindByProject() {
        createTicket("project1", "タスク1", "テスト");
        createTicket("project1", "タスク2", "テスト");
        createTicket("project2", "タスク3", "テスト");

        List<OpenTicket> project = ticketService.findOpenTicketByProject("project1");
        Assertions.assertThat(project).hasSize(2);
        Assertions.assertThat(project.get(0).title).isEqualTo("タスク1");
        Assertions.assertThat(project.get(0).ticketNum).isEqualTo(1);
        Assertions.assertThat(project.get(1).title).isEqualTo("タスク2");
        Assertions.assertThat(project.get(1).ticketNum).isEqualTo(2);
    }

    @Test
    public void testFindTicketModelByProject() {
        createTicket("project1", "タスク1", "テスト");
        createTicket("project1", "タスク2", "テスト");
        createTicket("project2", "タスク3", "テスト");

        List<TicketModel> project = ticketService.findOpenTicketModelByProject("project1");
        Assertions.assertThat(project).hasSize(2);
        Assertions.assertThat(project.get(0).getTitle()).isEqualTo("タスク1");
        Assertions.assertThat(project.get(0).getNumber()).isEqualTo(1);
        Assertions.assertThat(project.get(1).getTitle()).isEqualTo("タスク2");
        Assertions.assertThat(project.get(1).getNumber()).isEqualTo(2);

    }

}
