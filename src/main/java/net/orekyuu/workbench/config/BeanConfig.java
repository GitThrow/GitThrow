package net.orekyuu.workbench.config;

import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.UserService;
import net.orekyuu.workbench.service.impl.ProjectServiceImpl;
import net.orekyuu.workbench.service.impl.UserServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

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

//    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        return messageSource;
//    }
//
//    @Bean
//    public LocaleResolver localeResolver() {
//        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
//        return resolver;
//    }
}
