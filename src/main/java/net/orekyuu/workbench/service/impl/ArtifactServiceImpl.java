package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.Artifact;
import net.orekyuu.workbench.entity.dao.ArtifactDao;
import net.orekyuu.workbench.service.ArtifactService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class ArtifactServiceImpl implements ArtifactService {

    @Autowired
    private ArtifactDao artifactDao;

    @Value("${net.orekyuu.workbench.artifact-dir}")
    private String artifactDir;

    @Transactional(readOnly = false)
    @Override
    public Artifact save(String projectId, byte[] data, String fileName) {
        Artifact artifact = new Artifact(projectId, fileName);
        artifact = artifactDao.insert(artifact).getEntity();

        Path artifactDir = getArtifactFilePath(artifact);
        if (Files.exists(artifactDir)) {
            throw new RuntimeException(new FileAlreadyExistsException(artifactDir.toString()));
        }

        try {
            Files.createDirectories(artifactDir.getParent());
            Files.write(artifactDir, data, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return artifact;
    }

    @Transactional(readOnly = false)
    @Override
    public Artifact save(String projectId, InputStream in, String fileName) {
        Artifact artifact = new Artifact(projectId, fileName);
        artifact = artifactDao.insert(artifact).getEntity();

        Path artifactDir = getArtifactFilePath(artifact);
        if (Files.exists(artifactDir)) {
            throw new RuntimeException(new FileAlreadyExistsException(artifactDir.toString()));
        }

        try {
            Files.createDirectories(artifactDir.getParent());
            Files.copy(in, artifactDir);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return artifact;
    }

    private Path getArtifactFilePath(Artifact artifact) {
        Objects.requireNonNull(artifact.id);
        return Paths.get(artifactDir, artifact.projectId, artifact.id.toString(), artifact.fileName);
    }

    private Path getArtifactFilesDir(String projectId, int artifactId) {
        return Paths.get(artifactDir, projectId, String.valueOf(artifactId));
    }

    private Path getProjectDir(String projectId) {
        return Paths.get(artifactDir, projectId);
    }

    @Override
    public Optional<Artifact> findById(int id) {
        return artifactDao.findById(id);
    }

    @Override
    public List<Artifact> findByProjectId(String projectId) {
        return artifactDao.findByProject(projectId, Collectors.toList());
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteByProject(String projectId) {
        artifactDao.deleteByProject(projectId);
        Path projectDir = getProjectDir(projectId);
        if (Files.exists(projectDir)) {
            try {
                FileUtils.forceDelete(projectDir.toFile());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(Artifact artifact) {
        if (artifact == null) {
            return;
        }
        artifactDao.delete(artifact);
        Path artifactDir = getArtifactFilesDir(artifact.projectId, artifact.id);
        if (Files.exists(artifactDir)) {
            try {
                FileUtils.forceDelete(artifactDir.toFile());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public InputStream openArtifactStreamByArtifact(Artifact artifact) throws IOException {
        Path filePath = getArtifactFilePath(artifact);
        return Files.newInputStream(filePath);
    }

}
