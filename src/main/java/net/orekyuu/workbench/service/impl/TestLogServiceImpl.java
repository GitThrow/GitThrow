package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.TestLog;
import net.orekyuu.workbench.entity.dao.TestLogDao;
import net.orekyuu.workbench.service.TestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public class TestLogServiceImpl implements TestLogService {

    @Autowired
    private TestLogDao testLogDao;

    @Override
    public Optional<TestLog> findById(int id) {
        return testLogDao.findById(id);
    }

    @Override
    public List<TestLog> findByProject(String projectId) {
        return testLogDao.findByProject(projectId);
    }

    @Transactional(readOnly = false)
    @Override
    public void create(String projectId, String testLogJson) {
        testLogDao.insert(new TestLog(projectId, testLogJson));
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(TestLog testLog) {
        testLogDao.delete(testLog);
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteByProject(String projectId) {
        testLogDao.deleteByProject(projectId);
    }
}
