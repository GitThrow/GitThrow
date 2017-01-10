package net.orekyuu.gitthrow.job.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.orekyuu.gitthrow.build.model.domain.TestStatus;
import net.orekyuu.gitthrow.build.usecase.TestLogUsecase;
import net.orekyuu.gitthrow.job.JobMessenger;
import net.orekyuu.gitthrow.job.JobWorkspaceService;
import net.orekyuu.gitthrow.job.TestLogModel;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class SaveTestLogTask implements Task {

    @Autowired
    private TestLogUsecase testLogService;
    @Autowired
    private JobWorkspaceService jobWorkspaceService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        Optional<TestLogModel> opt = args.getData(TestTask.TEST_LOG_KEY);
        TestStatus status = args.<TestStatus>getData(TestTask.TEST_STATUS_KEY).orElseThrow(() -> new RuntimeException("status not found"));

        try (Repository repository = jobWorkspaceService.getRepository(args.getJobId())) {
            String head = repository.resolve("HEAD").name();
            opt.map(log -> {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    return mapper.writeValueAsString(log);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).ifPresent(json -> testLogService.create(args.getProject(), json, status, head));
        }

        return true;
    }
}
