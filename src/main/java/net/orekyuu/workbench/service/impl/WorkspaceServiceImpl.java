package net.orekyuu.workbench.service.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.RemoteRepositoryService;
import net.orekyuu.workbench.service.WorkspaceService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;

public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private RemoteRepositoryService remoteRepositoryService;

    @Value("${net.orekyuu.workbench.workspace-dir}")
    private String workspaceDir;

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

    private static final String DEFAULT_BRANCH = "master";

    @Override
    public void update(String projectId) throws ProjectNotFoundException, GitAPIException {
        Project project = projectService.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        Path path = getProjectWorkspaceDir(projectId);
        if (!Files.exists(path)) {
            createWorkspace(path, projectId);
            return;
        }


        Path workspaceDir = getProjectWorkspaceDir(projectId);
        try (Repository repository = getRepository(workspaceDir)){
            Git git = new Git(repository);
            //コンフリクトしてるとか中途半端な状態を戻す
            reset(git);

            //ブランチの一覧を取得
            List<Ref> branchResult = git.branchList()
                .setListMode(ListBranchCommand.ListMode.REMOTE)
                .call();

            //デフォルトブランチがなければ何もしない(まっさらなリポジトリとかの状況でありえる)
            Optional<Ref> defaultBranch = branchResult.stream()
                .filter(ref -> ref.getName().equals("refs/remotes/origin/" + DEFAULT_BRANCH))
                .findFirst();
            if (!defaultBranch.isPresent()) {
                return;
            }

            //最新の状態を更新
            PullResult result = git.pull()
                .setStrategy(MergeStrategy.RECURSIVE)
                .setRemote("origin")
                .setRemoteBranchName(DEFAULT_BRANCH).call();

            //失敗したら戻す
            if (!result.isSuccessful()) {
                reset(git);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void delete(String projectId) throws ProjectNotFoundException {
        projectService.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        Path dir = getProjectWorkspaceDir(projectId);
        if (Files.notExists(dir)) {
            return;
        }
        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void reset(Git git) throws GitAPIException {
        git.reset().setMode(ResetCommand.ResetType.HARD).call();
    }

    @Override
    public Path getProjectWorkspaceDir(String projectId) {
        return Paths.get(workspaceDir, projectId);
    }

    private void createWorkspace(Path path, String projectId) throws GitAPIException {
        try {
            Path remoteRepositoryDir = remoteRepositoryService.getProjectGitRepositoryDir(projectId);
            Files.createDirectories(path);
            Git call = Git.cloneRepository()
                .setDirectory(path.toFile())
                .setBare(false)
                .setURI("file://" + remoteRepositoryDir.toAbsolutePath().toString())
                .setCloneAllBranches(true)
                .call();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Repository getRepository(Path workspace) throws IOException {
        Repository repository = new FileRepositoryBuilder()
            .setWorkTree(workspace.toFile())
            .build();
        repository.incrementOpen();
        return repository;
    }
}
