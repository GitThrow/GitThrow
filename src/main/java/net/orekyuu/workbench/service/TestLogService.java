package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.TestLog;
import net.orekyuu.workbench.entity.TestStatus;

import java.util.List;
import java.util.Optional;

public interface TestLogService {

    Optional<TestLog> findById(int id);

    /**
     * プロジェクトに存在する全てのテストログを返します。ログの情報は含まれていないので必要な場合はfindByIdで取得してください。
     * @param projectId プロジェクトID
     * @return TestLog
     */
    List<TestLog> findByProject(String projectId);

    void create(String projectId, String testLogJson, TestStatus testStatus, String commit);

    void delete(TestLog testLog);

    void deleteByProject(String projectId);
}
