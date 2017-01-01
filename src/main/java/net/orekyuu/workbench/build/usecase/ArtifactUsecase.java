package net.orekyuu.workbench.build.usecase;

import net.orekyuu.workbench.build.model.domain.Artifact;
import net.orekyuu.workbench.build.port.ArtifactRepository;
import net.orekyuu.workbench.project.domain.model.Project;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ArtifactUsecase {

    private final ArtifactRepository artifactRepository;

    public ArtifactUsecase(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    @Transactional(readOnly = false)
    public Artifact create(Project project, String fileName, byte[] data) {
        return artifactRepository.create(project, fileName, data);
    }

    @Transactional(readOnly = false)
    public Artifact create(Project project, String fileName, InputStream data) {
        return artifactRepository.create(project, fileName, data);
    }

    @Transactional(readOnly = false)
    public Artifact delete(Artifact artifact, Project project) {
        return artifactRepository.delete(artifact, project);
    }

    @Transactional(readOnly = false)
    public void deleteByProject(Project project) {
        artifactRepository.deleteByProject(project);
    }

    public Optional<Pair<Artifact, InputStream>> findById(int id) {
        return artifactRepository.findById(id);
    }

    public List<Artifact> findByProject(Project project) {
        return artifactRepository.findByProject(project);
    }
}
