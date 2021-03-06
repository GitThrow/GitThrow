package net.orekyuu.gitthrow.job.task;

import net.orekyuu.gitthrow.build.usecase.WorkbenchConfigUsecase;
import net.orekyuu.gitthrow.job.BuildSettings;
import net.orekyuu.gitthrow.job.JobMessenger;
import net.orekyuu.gitthrow.job.JobWorkspaceService;
import net.orekyuu.gitthrow.job.WorkbenchConfig;
import net.orekyuu.gitthrow.job.message.BuildResult;
import net.orekyuu.gitthrow.job.message.LogMessage;
import net.orekyuu.gitthrow.project.domain.model.Project;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * ビルドを実行するタスク
 */
@Component
@Scope("prototype")
public class BuildTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(BuildTask.class);

    public static final String BUILD_RESULT_KEY = "BuildTask.result";
    public static final String TARGET_COMMIT_KEY = "BuildTask.commit";

    @Autowired
    private JobWorkspaceService jobWorkspaceService;
    @Autowired
    private WorkbenchConfigUsecase configService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        logger.info("Start BuildTask");
        try (Repository repository = jobWorkspaceService.getRepository(args.getJobId())) {
            String head = repository.resolve("HEAD").name();
            args.putData(TARGET_COMMIT_KEY, head);
        }

        Path workspacePath = jobWorkspaceService.getWorkspacePath(args.getJobId());
        List<String> command = command(args.getProject());
        if (command.isEmpty()) {
            messenger.send(new LogMessage("ビルド設定が有効化されていません"));
            return false;
        }

        boolean success = true;
        for (String s : command) {
            int code = exec(messenger, args, workspacePath, s);
            if (code != 0) {
                success = false;
            }
        }
        args.putData(BUILD_RESULT_KEY, success ? BuildResult.SUCCESS : BuildResult.FAILED);


        logger.info("End BuildTask");
        return true;
    }

    List<String> command(Project project) {
        return configService.find(project, "HEAD")
            .map(WorkbenchConfig::getBuildSettings)
            .map(BuildSettings::getBuildCommand)
            .orElseGet(ArrayList::new);
    }

    private int exec(JobMessenger jobMessenger, TaskArguments args, Path workspacePath, String command) {
        //ここOSとか環境によって動作が怪しい
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.directory(workspacePath.toFile());

        String charset = "UTF-8";

        try {
            Process start = processBuilder.start();
            Thread infoThread = new Thread(() -> {
                //コマンド実行後の標準出力をログに吐き出す
                try (Stream<LogMessage> in = new BufferedReader(new InputStreamReader(start.getInputStream(), charset))
                    .lines().map(LogMessage::new)) {
                    in.forEach(jobMessenger::send);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            infoThread.setDaemon(true);

            Thread errorThread = new Thread(() -> {
                //コマンド実行時のエラー出力をログに吐き出す
                try (Stream<LogMessage> in = new BufferedReader(new InputStreamReader(start.getErrorStream(), charset))
                    .lines().map(LogMessage::new)) {
                    in.forEach(jobMessenger::send);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            errorThread.setDaemon(true);

            infoThread.start();
            errorThread.start();
            //コマンドの実行が終了するまでここでThreadをブロックする
            return start.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
