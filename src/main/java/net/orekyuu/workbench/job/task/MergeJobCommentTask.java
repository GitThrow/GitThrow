package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.entity.Artifact;
import net.orekyuu.workbench.entity.TestStatus;
import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.message.BuildResult;
import net.orekyuu.workbench.service.TicketCommentService;
import net.orekyuu.workbench.util.BotUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class MergeJobCommentTask implements Task {

    private int commentTarget;

    @Autowired
    private TicketCommentService ticketCommentService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        String projectId = args.getProjectId();

        StringBuilder builder = new StringBuilder("### レポート");
        builder.append("\n");

        //ビルドステータス
        Optional<BuildResult> resultOpt = args.getData(BuildTask.BUILD_RESULT_KEY);
        builder.append("ビルドステータス: ");
        resultOpt.ifPresent(it -> builder.append(it == BuildResult.SUCCESS ? "成功" : "失敗"));
        builder.append("  \n");

        //ビルドしたコミット
        Optional<String> targetCommitOpt = args.getData(BuildTask.TARGET_COMMIT_KEY);
        builder.append("コミット: ");
        targetCommitOpt.ifPresent(builder::append);
        builder.append("  \n");

        //成果物のリンク
        Optional<Artifact> artifact = args.getData(SaveArtifactTask.ARTIFACT_KEY);
        artifact.ifPresent(it -> {
            String link = "/project/" + projectId + "/artifact/" + it.id;
            String message = String.format("成果物: [%s](%s)", it.fileName, link);
            builder.append(message);
        });
        builder.append("  \n");

        //テスト結果
        Optional<TestStatus> opt = args.getData(TestTask.TEST_STATUS_KEY);
        opt.ifPresent(it -> {
            builder.append("テスト結果: ").append(it == TestStatus.PASSING ? "PASSING" : "FAILED");
        });
        ticketCommentService.createComment(projectId, commentTarget, builder.toString(), BotUserUtil.toBotUserId(projectId));
        return true;
    }

    public void setCommentTarget(int commentTarget) {
        this.commentTarget = commentTarget;
    }
}
