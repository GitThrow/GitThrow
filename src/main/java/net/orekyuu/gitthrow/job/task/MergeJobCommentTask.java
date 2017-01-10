package net.orekyuu.gitthrow.job.task;

import net.orekyuu.gitthrow.build.model.domain.TestStatus;
import net.orekyuu.gitthrow.build.port.table.ArtifactTable;
import net.orekyuu.gitthrow.job.JobMessenger;
import net.orekyuu.gitthrow.job.message.BuildResult;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.ticket.domain.model.Ticket;
import net.orekyuu.gitthrow.ticket.usecase.TicketCommentUsecase;
import net.orekyuu.gitthrow.ticket.usecase.TicketUsecase;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import net.orekyuu.gitthrow.user.util.BotUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class MergeJobCommentTask implements Task {

    private int commentTarget;

    @Autowired
    private TicketCommentUsecase commentUsecase;
    @Autowired
    private TicketUsecase ticketUsecase;
    @Autowired
    private ProjectUsecase projectUsecase;
    @Autowired
    private UserUsecase userUsecase;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {

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
        Optional<ArtifactTable> artifact = args.getData(SaveArtifactTask.ARTIFACT_KEY);
        artifact.ifPresent(it -> {
            String link = "/project/" + args.getProject().getId() + "/artifact/" + it.getId();
            String message = String.format("成果物: [%s](%s)", it.getFileName(), link);
            builder.append(message);
        });
        builder.append("  \n");

        //テスト結果
        Optional<TestStatus> opt = args.getData(TestTask.TEST_STATUS_KEY);
        opt.ifPresent(it -> {
            builder.append("テスト結果: ").append(it == TestStatus.PASSING ? "PASSING" : "FAILED");
        });

        Ticket ticket = ticketUsecase.findById(args.getProject(), commentTarget).get();
        commentUsecase.create(
            ticket,
            builder.toString(),
            userUsecase.findById(BotUserUtil.toBotUserId(args.getProject().getId())).get());
        return true;
    }

    public void setCommentTarget(int commentTarget) {
        this.commentTarget = commentTarget;
    }
}
