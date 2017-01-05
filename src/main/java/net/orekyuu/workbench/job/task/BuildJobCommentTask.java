package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.build.port.table.ArtifactTable;
import net.orekyuu.workbench.controller.exception.ResourceNotFoundException;
import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.message.BuildResult;
import net.orekyuu.workbench.ticket.domain.model.Ticket;
import net.orekyuu.workbench.ticket.usecase.TicketCommentUsecase;
import net.orekyuu.workbench.ticket.usecase.TicketUsecase;
import net.orekyuu.workbench.user.usecase.UserUsecase;
import net.orekyuu.workbench.user.util.BotUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("request")
public class BuildJobCommentTask implements Task {

    private int commentTarget;

    @Autowired
    private TicketCommentUsecase commentUsecase;
    @Autowired
    private TicketUsecase ticketUsecase;
    @Autowired
    private UserUsecase userUsecase;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {

        StringBuilder builder = new StringBuilder("### ビルドレポート");
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

        Ticket ticket = ticketUsecase.findById(args.getProject(), commentTarget).orElseThrow(ResourceNotFoundException::new);
        commentUsecase.create(ticket, builder.toString(), userUsecase.findById(BotUserUtil.toBotUserId(args.getProject().getId())).get());
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
