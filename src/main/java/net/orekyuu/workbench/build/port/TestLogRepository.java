package net.orekyuu.workbench.build.port;

import net.orekyuu.workbench.build.model.domain.TestLog;
import net.orekyuu.workbench.build.model.domain.TestStatus;
import net.orekyuu.workbench.build.port.table.TestLogDao;
import net.orekyuu.workbench.build.port.table.TestLogTable;
import net.orekyuu.workbench.build.util.TestLogUtil;
import net.orekyuu.workbench.project.domain.model.Project;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TestLogRepository {

    private final TestLogDao logDao;

    public TestLogRepository(TestLogDao logDao) {
        this.logDao = logDao;
    }

    public TestLog create(Project project, String log, TestStatus status, String commit) {
        TestLogTable table = new TestLogTable(-1L, project.getId(),  log, LocalDateTime.now(), status, commit);
        TestLogTable result = logDao.insert(table).getEntity();
        return TestLogUtil.fromTable(result);
    }

    public TestLog delete(TestLog log) {
        TestLogTable entity = logDao.delete(new TestLogTable(
            log.getId(),
            log.getProjectId(),
            log.getLog(),
            log.getCreatedAt(),
            log.getStatus(),
            log.getCommit())).getEntity();
        return TestLogUtil.fromTable(entity);
    }

    public void deleteByProject(Project project) {
        logDao.deleteByProject(project.getId());
    }

    public Optional<TestLog> findById(int id) {
        return logDao.findById(id).map(TestLogUtil::fromTable);
    }

    public List<TestLog> findByProject(Project project) {
        return logDao.findByProject(project.getId()).stream()
            .map(TestLogUtil::fromTable)
            .collect(Collectors.toList());
    }

}
