package net.orekyuu.workbench.service;

import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Optional;

@Service
public interface WorkspaceService {

    /**
     * 指定のプロジェクトのワークスペースを最新にします
     * @param projectId プロジェクトID
     * @throws ProjectNotFoundException
     * @throws GitAPIException
     */
    void update(String projectId) throws ProjectNotFoundException, GitAPIException;

    /**
     * ワークスペースを削除します
     * @param projectId プロジェクトID
     * @throws ProjectNotFoundException
     */
    void delete(String projectId) throws ProjectNotFoundException;

    /**
     * @param projectId プロジェクトID
     * @return ワークスペースのパス
     */
    Path getProjectWorkspaceDir(String projectId);

    /**
     * masterブランチに存在するREADME.mdの内容を返す
     * @param projectId プロジェクトID
     * @return README.mdの内容
     * @throws ProjectNotFoundException
     * @throws GitAPIException
     */
    Optional<String> getReadmeFile(String projectId) throws ProjectNotFoundException, GitAPIException;
}
