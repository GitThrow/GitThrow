package net.orekyuu.workbench.service;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;

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
    Optional<OutputStream> getRepositoryFile(String projectId, String hash, Path relativePath) throws ProjectNotFoundException, GitAPIException;
    
}
