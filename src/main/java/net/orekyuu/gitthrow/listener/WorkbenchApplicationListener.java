package net.orekyuu.gitthrow.listener;

import net.orekyuu.gitthrow.service.exceptions.UserExistsException;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class WorkbenchApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserUsecase userUsecase;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            userUsecase.create("admin", "admin", "password", true);
        } catch (UserExistsException e) {
            //すでに存在している場合は何もしない
        }
    }
}
