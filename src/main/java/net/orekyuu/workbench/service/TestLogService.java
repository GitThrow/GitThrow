package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.TestLog;

import java.util.List;
import java.util.Optional;

public interface TestLogService {

    Optional<TestLog> findById(int id);

    List<TestLog> findByProject(String projectId);

    void create(String projectId, String testLogJson);

    void delete(TestLog testLog);

    void deleteByProject(String projectId);
}
