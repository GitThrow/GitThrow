package net.orekyuu.workbench.build.port;

import net.orekyuu.workbench.build.model.domain.Artifact;
import net.orekyuu.workbench.build.port.table.ArtifactDao;
import net.orekyuu.workbench.build.port.table.ArtifactTable;
import net.orekyuu.workbench.build.util.ArtifactUtil;
import net.orekyuu.workbench.project.domain.model.Project;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ArtifactRepository {

    private final ArtifactDao artifactDao;

    private String artifactDir;

    public ArtifactRepository(ArtifactDao artifactDao, @Value("${net.orekyuu.workbench.artifact-dir}")String artifactDir) {
        this.artifactDao = artifactDao;
        this.artifactDir = artifactDir;
    }

    public Artifact create(Project project, String fileName, byte[] data) {
        ArtifactTable result = artifactDao.insert(new ArtifactTable(-1, project.getId(), fileName)).getEntity();

        Path artifactDir = getArtifactFilePath(result);
        if (Files.exists(artifactDir)) {
            throw new RuntimeException(new FileAlreadyExistsException(artifactDir.toString()));
        }

        try {
            Files.createDirectories(artifactDir.getParent());
            Files.write(artifactDir, data, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return ArtifactUtil.fromTable(result);
    }

    public Artifact create(Project project, String fileName, InputStream data) {
        ArtifactTable result = artifactDao.insert(new ArtifactTable(-1, project.getId(), fileName)).getEntity();

        Path artifactDir = getArtifactFilePath(result);
        if (Files.exists(artifactDir)) {
            throw new RuntimeException(new FileAlreadyExistsException(artifactDir.toString()));
        }

        try {
            Files.createDirectories(artifactDir.getParent());
            Files.copy(data, artifactDir);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return ArtifactUtil.fromTable(result);
    }

    public Artifact delete(Artifact artifact, Project project) {
        ArtifactTable table = artifactDao.delete(new ArtifactTable(artifact.getId(), project.getId(), artifact.getFileName())).getEntity();

        Path artifactDir = getArtifactFilesDir(table.getProjectId(), table.getId());
        if (Files.exists(artifactDir)) {
            try {
                FileUtils.forceDelete(artifactDir.toFile());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return ArtifactUtil.fromTable(table);
    }

    public void deleteByProject(Project project) {
        artifactDao.deleteByProject(project.getId());
        Path projectDir = getProjectDir(project.getId());
        if (Files.exists(projectDir)) {
            try {
                FileUtils.forceDelete(projectDir.toFile());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    public Optional<Pair<Artifact, InputStream>> findById(int id) {
        return artifactDao.findById(id).map(table -> {
            Path filePath = getArtifactFilePath(table);
            try {
                return Pair.of(ArtifactUtil.fromTable(table), Files.newInputStream(filePath));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    public List<Artifact> findByProject(Project project) {
        return artifactDao.findByProject(project.getId(), Collectors.mapping(ArtifactUtil::fromTable, Collectors.toList()));
    }

    private Path getArtifactFilePath(ArtifactTable artifactTable) {
        Objects.requireNonNull(artifactTable.getId());
        return Paths.get(artifactDir, artifactTable.getProjectId(), artifactTable.getId().toString(), artifactTable.getFileName());
    }

    private Path getArtifactFilesDir(String projectId, int artifactId) {
        return Paths.get(artifactDir, projectId, String.valueOf(artifactId));
    }

    private Path getProjectDir(String projectId) {
        return Paths.get(artifactDir, projectId);
    }

}
