package net.orekyuu.workbench.job.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.TestLogModel;
import net.orekyuu.workbench.service.TestLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class SaveTestLogTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(SaveTestLogTask.class);

    @Autowired
    private TestLogService testLogService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        Optional<TestLogModel> opt = args.getData(TestTask.TEST_LOG_KEY);
        opt.map(log -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(log);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).ifPresent(json -> testLogService.create(args.getProjectId(), json));
        return false;
    }
}
