package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobContinuesImpossibleException;
import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.JobWorkspaceService;
import net.orekyuu.workbench.service.RemoteRepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
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
    private RemoteRepositoryService remoteRepositoryService;

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        Path path = service.getWorkspacePath(args.getJobId());
        service.createWorkspaceIfNotExists(args.getJobId());

        Path remoteRepositoryDir = remoteRepositoryService.getProjectGitRepositoryDir(args.getProjectId());
        try {
            Git git = Git.cloneRepository()
                .setDirectory(path.toFile())
                .setBare(false)
                .setURI("file://" + remoteRepositoryDir.toAbsolutePath().toString())
                .setCloneAllBranches(true)
                .setProgressMonitor(new SimpleLogProgressMonitor(messenger))
                .setBranch(branch == null ? "mater" : branch)
                .call();
            git.close();
        } catch (GitAPIException e) {
            e.printStackTrace();
             //cloneに失敗した場合は続行不可
            throw new JobContinuesImpossibleException(e.getMessage());
        }
        return true;
    }
}