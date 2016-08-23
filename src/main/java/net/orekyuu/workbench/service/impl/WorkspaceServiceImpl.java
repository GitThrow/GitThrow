package net.orekyuu.workbench.service.impl;

import com.sun.javafx.fxml.PropertyNotFoundException;
import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.service.GitService;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.WorkspaceService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;

public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private GitService gitService;

    @Value("${net.orekyuu.workbench.workspace-dir}")
    private String workspaceDir;

    private static final Logger logger = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

    private static final String DEFAULT_BRANCH = "master";

    @Override
    public void update(String projectId) throws ProjectNotFoundException, GitAPIException {
        Project project = projectService.findById(projectId).orElseThrow(PropertyNotFoundException::new);
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
        projectService.findById(projectId).orElseThrow(PropertyNotFoundException::new);
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

    @Override
    public Optional<String> getReadmeFile(String projectId) throws ProjectNotFoundException, GitAPIException {
        update(projectId);
        try (Repository repository = getRepository(getProjectWorkspaceDir(projectId))) {
            Git git = new Git(repository);
            //ブランチの一覧を取得
            List<Ref> branchResult = git.branchList()
                .setListMode(ListBranchCommand.ListMode.REMOTE)
                .call();

            //デフォルトブランチがなければ何もしない(まっさらなリポジトリとかの状況でありえる)
            Optional<Ref> defaultBranch = branchResult.stream()
                .filter(ref -> ref.getName().equals("refs/remotes/origin/" + DEFAULT_BRANCH))
                .findFirst();
            if (!defaultBranch.isPresent()) {
                return Optional.empty();
            }

            git.checkout().setName(DEFAULT_BRANCH).call();

            ObjectId head = repository.resolve("HEAD").toObjectId();
            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(head);
                RevTree tree = commit.getTree();

                Optional<String> result;

                try(TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    treeWalk.setFilter(PathFilter.create("README.md"));

                    if (!treeWalk.next()) {
                        return Optional.empty();
                    }

                    ObjectId objectId = treeWalk.getObjectId(0);

                    ObjectLoader objectLoader = repository.open(objectId);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    objectLoader.copyTo(out);

                    result = Optional.ofNullable(out.toString());
                }
                walk.dispose();
                return result;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void reset(Git git) throws GitAPIException {
        git.reset().setMode(ResetCommand.ResetType.HARD).call();
    }

    @Override
    public Path getProjectWorkspaceDir(String projectId) {
        String dir = workspaceDir;
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return Paths.get(dir + projectId);
    }

    private void createWorkspace(Path path, String projectId) throws GitAPIException {
        try {
            Path remoteRepositoryDir = gitService.getProjectGitRepositoryDir(projectId);
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
