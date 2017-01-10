package net.orekyuu.gitthrow.build.usecase;

import net.orekyuu.gitthrow.build.model.domain.TestLog;
import net.orekyuu.gitthrow.build.model.domain.TestStatus;
import net.orekyuu.gitthrow.build.port.TestLogRepository;
import net.orekyuu.gitthrow.project.domain.model.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TestLogUsecase {

    private final TestLogRepository logRepository;

    public TestLogUsecase(TestLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional(readOnly = false)
    public TestLog create(Project project, String log, TestStatus status, String commit) {
        return logRepository.create(project, log, status, commit);
    }

    @Transactional(readOnly = false)
    public TestLog delete(TestLog log) {
        return logRepository.delete(log);
    }

    @Transactional(readOnly = false)
    public void deleteByProject(Project project) {
        logRepository.deleteByProject(project);
    }

    public Optional<TestLog> findById(int id) {
        return logRepository.findById(id);
    }

    public List<TestLog> findByProject(Project project) {
        return logRepository.findByProject(project);
    }
}
