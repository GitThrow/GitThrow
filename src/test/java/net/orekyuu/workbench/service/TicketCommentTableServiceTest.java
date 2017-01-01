//package net.orekyuu.workbench.service;
//
//import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
//import net.orekyuu.workbench.service.exceptions.UserExistsException;
//import net.orekyuu.workbench.ticket.port.table.*;
//import net.orekyuu.workbench.util.TestRepositoryUtil;
//import org.assertj.core.api.Assertions;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.stream.Collectors;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//public class TicketCommentTableServiceTest {
//
//    @Autowired
//    private ProjectService projectService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private TicketService ticketService;
//    @Autowired
//    private TicketStatusDao statusDao;
//    @Autowired
//    private TicketPriorityDao priorityDao;
//    @Autowired
//    private TicketTypeDao typeDao;
//
//    @Autowired
//    private TicketCommentService commentService;
//
//    private User user1;
//    private User user2;
//
//    private TicketStatusTable status1;
//    private TicketPriorityTable priority1;
//    private TicketTypeTable type1;
//
//    @Autowired
//    private TestRepositoryUtil testRepositoryUtil;
//
//
//    private OpenTicketTable ticket1;
//    private OpenTicketTable ticket2;
//
//    @Before
//    public void before() throws UserExistsException, ProjectExistsException {
//        testRepositoryUtil.deleteGitRepositoryAndWorkspaceDir();
//
//        userService.createUser("user1", "user1", "pw");
//        userService.createUser("user2", "user2", "pw");
//
//        user1 = userService.findById("user1").get();
//        user2 = userService.findById("user2").get();
//
//        projectService.createProject("project1", "project1", user1);
//
//        status1 = statusDao.findByProject("project1", Collectors.toList()).get(0);
//        priority1 = priorityDao.findByProject("project1", Collectors.toList()).get(0);
//        type1 = typeDao.findByProject("project1", Collectors.toList()).get(0);
//
//        createTicketToProject1("ticket1", "");
//        createTicketToProject1("ticket2", "");
//
//        ticket1 = ticketService.findByProjectAndNum("project1", 1).get();
//        ticket2 = ticketService.findByProjectAndNum("project1", 2).get();
//    }
//
//    private void createTicketToProject1(String title, String desc) {
//        OpenTicketTable ticket = new OpenTicketTable();
//        ticket.project = "project1";
//        ticket.title = title;
//        ticket.description = desc;
//        ticket.assignee = user1.id;
//        ticket.proponent = user1.id;
//        ticket.type = type1.id;
//        ticket.priority = priority1.id;
//        ticket.status = status1.id;
//        ticket.limit = LocalDateTime.now();
//        ticketService.createTicket(ticket);
//    }
//
//    @Test
//    public void testCreate() {
//        Assertions.assertThat(commentService.findByTicket("project1", ticket1.ticketNum)).hasSize(0);
//
//        commentService.createComment("project1", ticket1.ticketNum, "hoge", user1.id);
//        Assertions.assertThat(commentService.findByTicket("project1", ticket1.ticketNum))
//            .hasSize(1)
//            .allMatch(comment -> comment.text.equals("hoge"));
//    }
//
//    @Test
//    public void testDeleteByProject() {
//        Assertions.assertThat(commentService.findByTicket("project1", ticket1.ticketNum)).hasSize(0);
//
//        commentService.createComment("project1", ticket1.ticketNum, "hoge", user1.id);
//        commentService.createComment("project1", ticket1.ticketNum, "hoge", user1.id);
//        commentService.createComment("project1", ticket1.ticketNum, "hoge", user1.id);
//
//        Assertions.assertThat(commentService.findByTicket("project1", ticket1.ticketNum)).hasSize(3);
//
//        commentService.deleteByProject("project1");
//
//        Assertions.assertThat(commentService.findByTicket("project1", ticket1.ticketNum)).hasSize(0);
//    }
//
//    @Test
//    public void testFindByProjectAndTicketNum() {
//        Assertions.assertThat(commentService.findByTicket("project1", ticket1.ticketNum)).hasSize(0);
//        Assertions.assertThat(commentService.findByTicket("project1", ticket2.ticketNum)).hasSize(0);
//
//        commentService.createComment("project1", ticket1.ticketNum, "hoge", user1.id);
//
//        Assertions.assertThat(commentService.findByTicket("project1", ticket1.ticketNum)).hasSize(1);
//        Assertions.assertThat(commentService.findByTicket("project1", ticket2.ticketNum)).hasSize(0);
//    }
//}
