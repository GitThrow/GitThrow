package net.orekyuu.gitthrow.job.task;


import net.orekyuu.gitthrow.build.model.domain.TestLog;
import net.orekyuu.gitthrow.controller.exception.ResourceNotFoundException;
import net.orekyuu.gitthrow.job.JobMessenger;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestCommentUsecase;
import net.orekyuu.gitthrow.pullrequest.usecase.PullRequestUsecase;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import net.orekyuu.gitthrow.user.util.BotUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class TestJobCommentTask implements Task {

    private int commentTarget;

    @Autowired
    private PullRequestCommentUsecase commentUsecase;
    @Autowired
    private PullRequestUsecase prUsecase;
    @Autowired
    private UserUsecase userUsecase;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {

        TestLog testLog = args.<TestLog>getData(SaveTestLogTask.TEST_LOG_KEY).get();

        StringBuilder builder = new StringBuilder("### テストレポート");
        builder.append("\n");

        //ビルドステータス
        builder.append("テストステータス: ");
        builder.append(testLog.getStatus().name());
        builder.append("  \n");

        //ビルドしたコミット
        builder.append("[Link](/project/").append(testLog.getProjectId()).append("/test/").append(testLog.getId()).append(")");


        PullRequest pr = prUsecase.findById(args.getProject(), commentTarget).orElseThrow(ResourceNotFoundException::new);
        commentUsecase.create(pr, builder.toString(), userUsecase.findById(BotUserUtil.toBotUserId(args.getProject().getId())).get());
        return true;
    }

    /**
     * コメント先を設定します
     * @param commentTarget コメント先
     */
    public void setCommentTarget(int commentTarget) {
        this.commentTarget = commentTarget;
    }
}
