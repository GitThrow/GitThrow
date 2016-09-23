package net.orekyuu.workbench.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.orekyuu.workbench.service.RemoteRepositoryService;
import net.orekyuu.workbench.service.WorkbenchConfig;
import net.orekyuu.workbench.service.WorkbenchConfigService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

public class WorkbenchConfigServiceImpl implements WorkbenchConfigService {

    @Autowired
    private RemoteRepositoryService repositoryService;

    public static final String WORKBENCH_CONFIG_PATH = ".workbenchconfig.json";

    @Override
    public Optional<WorkbenchConfig> find(String projectId, String hash) {
        try {
            return repositoryService.getRepositoryFile(projectId, hash, WORKBENCH_CONFIG_PATH)
                .map(stream -> {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        return mapper.readValue(stream.toByteArray(), WorkbenchConfig.class);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        } catch (ProjectNotFoundException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }
}
