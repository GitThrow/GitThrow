package net.orekyuu.gitthrow.build.usecase;

import net.orekyuu.gitthrow.build.model.domain.Artifact;
import net.orekyuu.gitthrow.build.port.ArtifactRepository;
import net.orekyuu.gitthrow.build.port.table.ArtifactDao;
import net.orekyuu.gitthrow.build.util.ArtifactUtil;
import net.orekyuu.gitthrow.project.domain.model.Project;
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
    private final ArtifactDao artifactDao;

    public ArtifactUsecase(ArtifactRepository artifactRepository, ArtifactDao artifactDao) {
        this.artifactRepository = artifactRepository;
        this.artifactDao = artifactDao;
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
    public Artifact delete(int artifactId, Project project) {
        Artifact artifact = artifactDao.findById(artifactId).map(ArtifactUtil::fromTable).get();
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
