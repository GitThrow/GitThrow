package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GitCloneTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(GitCloneTask.class);
    private String branch;

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public void process(JobMessenger messenger, TaskArguments args) throws Exception {
        logger.info("Start GitCloneTask");
        for (int i = 0; i < 100; i+=10) {
            messenger.send((i)+"%...");
            Thread.sleep(100);
        }
        logger.info("End GitCloneTask");
    }
}
