package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ビルドを実行するタスク
 */
@Component
@Scope("prototype")
public class BuildTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(BuildTask.class);
    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        logger.info("Start BuildTask");
        for (int i = 0; i < 100; i+=10) {
            messenger.send(i+"%");
            Thread.sleep(100);
        }
        logger.info("End BuildTask");
        return true;
    }
}
