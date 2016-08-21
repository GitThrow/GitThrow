package net.orekyuu.workbench.config;

import net.orekyuu.workbench.service.*;
import net.orekyuu.workbench.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
