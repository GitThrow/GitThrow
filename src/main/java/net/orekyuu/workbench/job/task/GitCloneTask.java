package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.JobWorkspaceService;
import net.orekyuu.workbench.service.GitService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

/**
 * リポジトリをcloneしてくるタスク
 */
@Component
@Scope("request")
public class GitCloneTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(GitCloneTask.class);
    private String branch;

    @Autowired
    private JobWorkspaceService service;
    @Autowired
    private GitService gitService;

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        Path path = service.getWorkspacePath(args.getJobId());
        service.createWorkspaceIfNotExists(args.getJobId());

        Path remoteRepositoryDir = gitService.getProjectGitRepositoryDir(args.getProjectId());
        try {
            Git git = Git.cloneRepository()
                .setDirectory(path.toFile())
                .setBare(false)
                .setURI("file://" + remoteRepositoryDir.toAbsolutePath().toString())
                .setCloneAllBranches(true)
                .setProgressMonitor(new ProgressMonitor() {
                    @Override
                    public void start(int i) {
                    }

                    @Override
                    public void beginTask(String s, int i) {
                        messenger.send(s);
                    }

                    @Override
                    public void update(int i) {

                    }

                    @Override
                    public void endTask() {
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }
                })
                .setBranch(branch == null ? "mater" : branch)
                .call();
            git.close();
        } catch (GitAPIException e) {
            e.printStackTrace();
            messenger.send(e.getMessage());
            return false;
        }
        return true;
    }
}
