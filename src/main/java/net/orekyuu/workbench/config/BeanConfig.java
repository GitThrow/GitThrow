package net.orekyuu.workbench.config;

import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.TicketService;
import net.orekyuu.workbench.service.UserService;
import net.orekyuu.workbench.service.impl.ProjectServiceImpl;
import net.orekyuu.workbench.service.impl.TicketServiceImpl;
import net.orekyuu.workbench.service.impl.UserServiceImpl;
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
}
