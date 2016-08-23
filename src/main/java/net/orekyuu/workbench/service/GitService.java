package net.orekyuu.workbench.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public interface GitService {

    Path getProjectGitRepositoryDir(String projectId);

    /**
     * リモートリポジトリを作成します
     * @param projectId プロジェクトID
     */
    void createRemoteRepository(String projectId);

}
