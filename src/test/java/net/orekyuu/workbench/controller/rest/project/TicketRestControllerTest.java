package net.orekyuu.workbench.controller.rest.project;

import net.orekyuu.workbench.controller.rest.RestApiTest;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.domain.model.TicketPriority;
import net.orekyuu.workbench.ticket.domain.model.TicketStatus;
import net.orekyuu.workbench.ticket.domain.model.TicketType;
import net.orekyuu.workbench.ticket.usecase.TicketUsecase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TicketRestControllerTest extends RestApiTest{
    @Autowired
    private TicketUsecase ticketUsecase;

    private List<TicketPriority> priorityList;
    private List<TicketStatus> statusList;
    private List<TicketType> typeList;

    @Override
    public void onSetup() throws Exception {
        priorityList = ticketUsecase.findPriorityByProject(project);
        statusList = ticketUsecase.findStatusByProject(project);
        typeList = ticketUsecase.findTypeByProject(project);
    }

    @Test
    public void all() throws Exception {
        ticketUsecase.create(project, "test1", "test1", user1, user1, null, typeList.get(0), statusList.get(0), priorityList.get(0));
        ticketUsecase.create(project, "test2", "test2", user1, user1, null, typeList.get(0), statusList.get(0), priorityList.get(0));

        getMvc("/rest/project/ticket", user1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andDo(document);
    }

    @Test
    public void find() throws Exception {
        ticketUsecase.create(project, "test1", "test1", user1, user1, null, typeList.get(0), statusList.get(0), priorityList.get(0));

        getMvc("/rest/project/ticket/1", user1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", equalTo("test1")))
            .andDo(document);

        getMvc("/rest/project/ticket/2", user1)
            .andExpect(status().isNotFound());

        getMvc("/rest/project/ticket/1", user2)
            .andExpect(status().isForbidden());
    }

    @Test
    public void create() throws Exception {
        Assertions.assertThat(ticketUsecase.findByProject(project)).hasSize(0);

        postMvc("/rest/project/ticket", "{\n" +
            "    \"title\":\"test1\",\n" +
            "    \"description\":\"test1\",\n" +
            "    \"assignee\": {\"id\": \"user1\"},\n" +
            "    \"type\":{\"id\": " + typeList.get(0).getId() + "},\n" +
            "    \"status\":{\"id\": " + statusList.get(0).getId() + "},\n" +
            "    \"priority\":{\"id\": " + priorityList.get(0).getId() + "}\n" +
            "}", user1)
            .andExpect(status().isOk())
            .andDo(document);

        Assertions.assertThat(ticketUsecase.findByProject(project)).hasSize(1);
    }

    @Test
    public void update() throws Exception {
        ticketUsecase.create(project, "test1", "test1", user1, user1, null, typeList.get(0), statusList.get(0), priorityList.get(0));

        putMvc("/rest/project/ticket/1", "{\n" +
            "    \"title\":\"hoge\",\n" +
            "    \"description\":\"test1\",\n" +
            "    \"assignee\": {\"id\": \"user1\"},\n" +
            "    \"type\":{\"id\": " + typeList.get(0).getId() + "},\n" +
            "    \"status\":{\"id\": " + statusList.get(0).getId() + "},\n" +
            "    \"priority\":{\"id\": " + priorityList.get(0).getId() + "}\n" +
            "}", user1)
            .andExpect(status().isOk())
            .andDo(document);

        Assertions.assertThat(ticketUsecase.findById(project, 1))
            .map(Ticket::getTitle)
            .hasValue("hoge");
    }

}
