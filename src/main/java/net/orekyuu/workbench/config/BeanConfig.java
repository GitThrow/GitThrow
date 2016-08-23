package net.orekyuu.workbench.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.orekyuu.workbench.service.*;
import net.orekyuu.workbench.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfig {

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public ProjectService projectService() {
        return new ProjectServiceImpl();
    }

    @Bean
    public TicketService ticketService() {
        return new TicketServiceImpl();
    }

    @Bean
    public TicketTypeService ticketTypeService() {
        return new TicketTypeServiceImpl();
    }

    @Bean
    public TicketStatusService ticketStatusService() {
        return new TicketStatusServiceImpl();
    }

    @Bean
    public TicketPriorityService ticketPriorityService() {
        return new TicketPriorityServiceImpl();
    }

    @Bean
    public WorkspaceService workspaceService() {
        return new WorkspaceServiceImpl();
    }

    @Bean
    public GitService gitService() {
        return new GitServiceImpl();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
