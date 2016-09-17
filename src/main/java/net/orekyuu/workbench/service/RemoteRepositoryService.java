package net.orekyuu.workbench.service;

import net.orekyuu.workbench.git.FileDiff;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.util.List;
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

    /**
     *
     * @param projectId
     * @param hash ハッシュもしくはブランチ
     * @param relativePath リポジトリルートからのファイルパス
     * @return 指定されたファイルパスの内容
     * @throws ProjectNotFoundException
     * @throws GitAPIException
     */
    Optional<ByteArrayOutputStream> getRepositoryFile(String projectId, String hash, String relativePath) throws ProjectNotFoundException, GitAPIException;


    /**
     * ブランチの差分
     * @param projectId プロジェクトID
     * @param baseBranch ベースのブランチ
     * @param targetBranch ターゲットのブランチ
     */
    List<DiffEntry> diff(String projectId, String baseBranch, String targetBranch) throws GitAPIException;

    /**
     * マージが出来るかチェックします
     * @param projectId プロジェクトID
     * @param baseBranch ベースのブランチ
     * @param targetBranch ターゲットのブランチ
     * @return マージできるならtrue
     */
    boolean checkConflict(String projectId, String baseBranch, String targetBranch);

    /**
     * ファイルDiffを求める
     * @param projectId プロジェクトID
     * @param entry DiffEntry
     * @return ファイルのDiff
     * @throws GitAPIException
     */
    FileDiff calcFileDiff(String projectId, DiffEntry entry) throws GitAPIException;

    /**
     * リモートリポジトリに存在するブランチの一覧を取得
     * @param projectId プロジェクトID
     * @return リモートリポジトリに存在するブランチの一覧
     */
    List<String> findBranch(String projectId);
}
