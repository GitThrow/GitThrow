package net.orekyuu.workbench.listener;

import net.orekyuu.workbench.service.UserService;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class WorkbenchApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            userService.createUser("admin", "admin", "password", true);
        } catch (UserExistsException e) {
            //すでに存在している場合は何もしない
        }
    }
}
