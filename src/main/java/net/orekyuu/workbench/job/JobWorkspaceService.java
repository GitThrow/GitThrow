package net.orekyuu.workbench.job;

import org.eclipse.jgit.lib.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

/**
 * ジョブ実行時のワークスペースを管理するサービス
 */
@Service
public interface JobWorkspaceService {

    /**
     * ワークスペースのパスを返す
     * @param jobId ジョブID
     * @return ワークスペースのパス
     */
    Path getWorkspacePath(UUID jobId);

    /**
     * @param jobId ジョブID
     * @return ワークスペースのリポジトリ
     * @throws IOException
     */
    Repository getRepository(UUID jobId) throws IOException;

    /**
     * ワークスペースが存在しなければ作成する
     * @param jobId ジョブID
     * @throws IOException
     */
    void createWorkspaceIfNotExists(UUID jobId) throws IOException;

    /**
     * ワークスペースが存在していれば削除する
     * @param jobId ジョブID
     * @throws IOException
     */
    void deleteWorkspaceIfExists(UUID jobId) throws IOException;

}
