package net.orekyuu.gitthrow.build.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.orekyuu.gitthrow.git.domain.RemoteRepository;
import net.orekyuu.gitthrow.git.domain.RemoteRepositoryFactory;
import net.orekyuu.gitthrow.job.WorkbenchConfig;
import net.orekyuu.gitthrow.project.domain.model.Project;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

@Service
public class WorkbenchConfigUsecase {

    private final RemoteRepositoryFactory factory;
    public static final String WORKBENCH_CONFIG_PATH = "workbenchconfig.json";

    public WorkbenchConfigUsecase(RemoteRepositoryFactory factory) {
        this.factory = factory;
    }

    public Optional<WorkbenchConfig> find(Project project, String hash) {
        try {
            RemoteRepository repository = factory.create(project.getId());
            return repository.getRepositoryFile(hash, WORKBENCH_CONFIG_PATH).map(stream -> {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    return mapper.readValue(stream.toByteArray(), WorkbenchConfig.class);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }
}
