package net.orekyuu.workbench.service;


import net.orekyuu.workbench.entity.Artifact;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface ArtifactService {

    /**
     * データを保存する
     * @param projectId プロジェクトID
     * @param data データ
     */
    void save(String projectId, byte[] data, String fileName);

    void save(String projectId, InputStream in, String fileName);

    Optional<Artifact> findById(int id);

    List<Artifact> findByProjectId(String projectId);

    void deleteByProject(String projectId);

    void deleteById(int id);

    InputStream openArtifactStreamByArtifact(Artifact artifact) throws IOException;

}
