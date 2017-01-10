package net.orekyuu.gitthrow.ticket.usecase;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.ticket.domain.model.Ticket;
import net.orekyuu.gitthrow.ticket.domain.model.TicketPriority;
import net.orekyuu.gitthrow.ticket.domain.model.TicketStatus;
import net.orekyuu.gitthrow.ticket.domain.model.TicketType;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import net.orekyuu.gitthrow.util.ProjectTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TicketUsecaseTest {

    @Autowired
    private ProjectUsecase projectUsecase;
    @Autowired
    private TicketUsecase ticketUsecase;
    @Autowired
    private UserUsecase userUsecase;

    @Autowired
    private ProjectTestUtil util;


    private User user1;
    private User user2;
    private Project project;
    private List<TicketPriority> priorityList;
    private List<TicketStatus> statusList;
    private List<TicketType> typeList;

    @Before
    public void setUp() throws Exception {
        user1 = userUsecase.create("user1", "user1", "password");
        user2 = userUsecase.create("user2", "user2", "password");

        project = projectUsecase.createProject("project1", "project1", user1);
        projectUsecase.join(project, user2);

        priorityList = ticketUsecase.findPriorityByProject(project);
        statusList = ticketUsecase.findStatusByProject(project);
        typeList = ticketUsecase.findTypeByProject(project);
    }

    @After
    public void tearDown() throws Exception {
        util.deleteGitRepositoryAndWorkspaceDir();
    }

    @Test
    public void create() throws Exception {
        Project project2 = projectUsecase.createProject("project2", "project2", user1);
        Assertions.assertThat(ticketUsecase.findByProject(this.project)).hasSize(0);
        Ticket ticket1 = ticketUsecase.create(this.project, "test1", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));
        Ticket ticket2 = ticketUsecase.create(this.project, "test2", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));

        Ticket ticket3 = ticketUsecase.create(project2, "test3", "test3", user1, user1, null,
            ticketUsecase.findTypeByProject(project2).get(0),
            ticketUsecase.findStatusByProject(project2).get(0),
            ticketUsecase.findPriorityByProject(project2).get(0)
        );
        Assertions.assertThat(ticketUsecase.findByProject(this.project)).hasSize(2);


        Assertions.assertThat(ticketUsecase.findById(this.project, ticket1.getTicketNum()))
            .isPresent()
            .map(Ticket::getTicketNum)
            .hasValue(1);

        Assertions.assertThat(ticketUsecase.findById(this.project, ticket2.getTicketNum()))
            .isPresent()
            .map(Ticket::getTicketNum)
            .hasValue(2);

        Assertions.assertThat(ticketUsecase.findById(project2, ticket3.getTicketNum()))
            .isPresent()
            .map(Ticket::getTicketNum)
            .hasValue(1);
    }

    @Test
    public void update() throws Exception {
        Ticket ticket = ticketUsecase.create(project, "test", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));
        ticket.setAssignee(user1);
        ticket.setTitle("hoge");
        ticket.setDescription("desc");
        ticket.setType(typeList.get(1));
        ticket.setStatus(statusList.get(1));
        ticket.setPriority(priorityList.get(1));
        ticket.setLimit(LocalDateTime.of(2017, 1, 1, 0, 0));

        ticket = ticketUsecase.update(ticket);
        Assertions.assertThat(ticket.getTitle()).isEqualTo("hoge");
        Assertions.assertThat(ticket.getDescription()).isEqualTo("desc");
        Assertions.assertThat(ticket.getType()).isEqualTo(typeList.get(1));
        Assertions.assertThat(ticket.getStatus()).isEqualTo(statusList.get(1));
        Assertions.assertThat(ticket.getPriority()).isEqualTo(priorityList.get(1));
        Assertions.assertThat(ticket.getLimit()).isEqualTo(LocalDateTime.of(2017, 1, 1, 0, 0));

        ticket = ticketUsecase.findById(project, ticket.getTicketNum()).get();
        Assertions.assertThat(ticket.getTitle()).isEqualTo("hoge");
        Assertions.assertThat(ticket.getDescription()).isEqualTo("desc");
        Assertions.assertThat(ticket.getType()).isEqualTo(typeList.get(1));
        Assertions.assertThat(ticket.getStatus()).isEqualTo(statusList.get(1));
        Assertions.assertThat(ticket.getPriority()).isEqualTo(priorityList.get(1));
        Assertions.assertThat(ticket.getLimit()).isEqualTo(LocalDateTime.of(2017, 1, 1, 0, 0));
    }

    @Test
    public void findByProject() throws Exception {
        ticketUsecase.create(project, "test", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));
        ticketUsecase.create(project, "test", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));

        List<Ticket> tickets = ticketUsecase.findByProject(project);
        Assertions.assertThat(tickets).hasSize(2);
    }

    @Test
    public void findById() throws Exception {
        Ticket ticket = ticketUsecase.create(project, "test", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));
        Assertions.assertThat(ticketUsecase.findById(project, ticket.getTicketNum()))
            .isPresent()
            .map(Ticket::getTitle)
            .hasValue("test");
    }

    @Test
    public void deleteByProject() throws Exception {
        ticketUsecase.create(project, "test", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));
        ticketUsecase.create(project, "test", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));

        Assertions.assertThat(ticketUsecase.findByProject(project)).hasSize(2);
        ticketUsecase.deleteByProject(project);

        Assertions.assertThat(ticketUsecase.findByProject(project)).hasSize(0);
        Assertions.assertThat(ticketUsecase.findPriorityByProject(project)).isNotEmpty();
    }

    @Test
    public void deleteAllByProject() throws Exception {
        ticketUsecase.create(project, "test", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));
        ticketUsecase.create(project, "test", "test", user2, user1, LocalDateTime.now(), typeList.get(0), statusList.get(0), priorityList.get(0));

        Assertions.assertThat(ticketUsecase.findByProject(project)).hasSize(2);
        ticketUsecase.deleteAllByProject(project);

        Assertions.assertThat(ticketUsecase.findByProject(project)).isEmpty();
        Assertions.assertThat(ticketUsecase.findPriorityByProject(project)).isEmpty();
    }

    @Test
    public void findPriorityByProject() throws Exception {
        Assertions.assertThat(ticketUsecase.findPriorityByProject(project)).isNotEmpty();
    }

    @Test
    public void findPriorityById() throws Exception {
        Assertions.assertThat(ticketUsecase.findPriorityById(project, priorityList.get(0).getId())).isNotEmpty();
    }

    @Test
    public void findTypeByProject() throws Exception {
        Assertions.assertThat(ticketUsecase.findTypeByProject(project)).isNotEmpty();
    }

    @Test
    public void findTypeById() throws Exception {
        Assertions.assertThat(ticketUsecase.findTypeById(project, typeList.get(0).getId())).isNotEmpty();
    }

    @Test
    public void findStatusByProject() throws Exception {
        Assertions.assertThat(ticketUsecase.findStatusByProject(project)).isNotEmpty();
    }

    @Test
    public void findStatusById() throws Exception {
        Assertions.assertThat(ticketUsecase.findStatusById(project, statusList.get(0).getId())).isNotEmpty();
    }

}
