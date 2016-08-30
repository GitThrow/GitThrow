package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.JobWorkspaceService;
import net.orekyuu.workbench.job.message.LogMessage;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * ビルドを実行するタスク
 */
@Component
@Scope("prototype")
public class BuildTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(BuildTask.class);

    @Autowired
    private JobWorkspaceService jobWorkspaceService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        logger.info("Start BuildTask");
        Path workspacePath = jobWorkspaceService.getWorkspacePath(args.getJobId());
        List<String> command = command();
        for (String s : command) {
            exec(messenger, args, workspacePath, s);
        }
        logger.info("End BuildTask");
        return true;
    }

    List<String> command() {
        //TODO DBからコマンドの一覧を得て返す
        return new ArrayList<>(Arrays.asList("mvnw package"));
    }

    private void exec(JobMessenger jobMessenger, TaskArguments args, Path workspacePath, String command) {
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
            start.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
