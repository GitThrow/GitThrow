package net.orekyuu.workbench.service;

import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Optional;

@Service
public interface RemoteRepositoryService {

    Path getProjectGitRepositoryDir(String projectId);

    /**
     * リモートリポジトリを作成します
     * @param projectId プロジェクトID
     */
    void createRemoteRepository(String projectId);

    /**
     * masterブランチに存在するREADME.mdの内容を返す
     * @param projectId プロジェクトID
     * @return README.mdの内容
     * @throws ProjectNotFoundException
     * @throws GitAPIException
     */
    Optional<String> getReadmeFile(String projectId) throws ProjectNotFoundException, GitAPIException;

}
