package net.orekyuu.workbench.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.orekyuu.workbench.service.RemoteRepositoryService;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;

public class RemoteRepositoryServiceImpl implements RemoteRepositoryService {

    private static final String DEFAULT_BRANCH = "master";
    @Autowired
    private Properties applicationProperties;

    @Value("${net.orekyuu.workbench.repository-dir}")
    private String gitDir;

    @Override
    public Path getProjectGitRepositoryDir(String projectId) {
        return Paths.get(gitDir, projectId);
    }

    @Override
    public void createRemoteRepository(String projectId) {
        Path repositoryDir = getProjectGitRepositoryDir(projectId);
        if (Files.exists(repositoryDir)) {
            IOException exception = new IOException("すでに存在している: " + projectId);
            throw new UncheckedIOException(exception);
        }

        try {
            Files.createDirectories(repositoryDir);

            Repository repo = new FileRepositoryBuilder().setGitDir(repositoryDir.toFile()).setBare().build();
            final boolean isBare = true;
            repo.create(isBare);

            StoredConfig config = repo.getConfig();
            config.setBoolean("http", null, "receivepack", true);
            config.save();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Repository getRepository(Path path) throws IOException {
        Repository repo = new FileRepositoryBuilder().setGitDir(path.toFile()).setBare().build();
        repo.incrementOpen();
        return repo;
    }

    @Override
    public Optional<String> getReadmeFile(String projectId) throws ProjectNotFoundException, GitAPIException {

        try (Repository repository = getRepository(getProjectGitRepositoryDir(projectId)); Git git = new Git(repository)) {
            // ブランチの一覧を取得
            List<Ref> branchResult = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

            String targetBranch = "refs/heads/" + DEFAULT_BRANCH;

            // デフォルトブランチがなければ何もしない(まっさらなリポジトリとかの状況でありえる)
            if (branchResult.stream().noneMatch(ref -> ref.getName().equals(targetBranch))) {
                return Optional.empty();
            }
            return getRepositoryFile(projectId, targetBranch, "README.md").map((bytes) -> bytes.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Optional<ByteArrayOutputStream> getRepositoryFile(String projectId, String hash, String relativePath) throws ProjectNotFoundException,
            GitAPIException {
        try (Repository repository = getRepository(getProjectGitRepositoryDir(projectId))) {
            // ハッシュかその他で分岐
            // タグとかブランチも食える模様
            ObjectId commitId = ObjectId.isId(hash)?ObjectId.fromString(hash):repository.resolve(hash);
            if(commitId==null){
                return Optional.empty();
            }
            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                RevTree tree = commit.getTree();

                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setFilter(PathFilter.create(relativePath));
                    if (!treeWalk.next()) {
                        return Optional.empty();
                    }
                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader objectLoader = repository.open(objectId);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    objectLoader.copyTo(outputStream);
                    revWalk.dispose();
                    return Optional.of(outputStream);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }
}
