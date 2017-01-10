package net.orekyuu.gitthrow.job.task;

import net.orekyuu.gitthrow.build.model.domain.TestStatus;
import net.orekyuu.gitthrow.build.usecase.WorkbenchConfigUsecase;
import net.orekyuu.gitthrow.job.*;
import net.orekyuu.gitthrow.job.message.LogMessage;
import net.orekyuu.gitthrow.job.message.TestResult;
import net.orekyuu.gitthrow.job.message.TestResultMessage;
import net.orekyuu.gitthrow.project.domain.model.Project;
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
 * テストを実行するタスク
 */
@Component
@Scope("prototype")
public class TestTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(TestTask.class);

    public static final String TEST_LOG_KEY = "testlog";
    public static final String TEST_STATUS_KEY = "teststatus";

    @Autowired
    private JobWorkspaceService jobWorkspaceService;
    @Autowired
    private WorkbenchConfigUsecase configService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {
        logger.info("Start TestTask");
        Path workspacePath = jobWorkspaceService.getWorkspacePath(args.getJobId());
        List<String> command = command(args.getProject());
        if (command.isEmpty()) {
            messenger.send(new LogMessage("テスト設定が有効化されていません"));
            return false;
        }

        boolean isPassing = true;
        for (String s : command) {
            int responseCode = exec(messenger, args, workspacePath, s);
            if (responseCode != 0) {
                isPassing = false;
            }
        }

        messenger.send(new TestResultMessage(isPassing ? TestResult.PASSING : TestResult.FAILED));

        logger.info("End TestTask");
        return true;
    }

    List<String> command(Project project) {
        return configService.find(project, "HEAD")
            .map(WorkbenchConfig::getTestSettings)
            .map(TestSettings::getTestCommand)
            .orElseGet(ArrayList::new);
    }

    private int exec(JobMessenger jobMessenger, TaskArguments args, Path workspacePath, String command) {
        //ここOSとか環境によって動作が怪しい
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.directory(workspacePath.toFile());

        String charset = "UTF-8";

        List<TestLogModel.Line> lines = new ArrayList<>();
        try {
            Process start = processBuilder.start();
            Thread infoThread = new Thread(() -> {
                //コマンド実行後の標準出力をログに吐き出す
                try (Stream<LogMessage> in = new BufferedReader(new InputStreamReader(start.getInputStream(), charset))
                    .lines()
                    .peek(line -> lines.add(new TestLogModel.Line(line, TestLogModel.OutputType.DEFAULT)))
                    .map(LogMessage::new)) {
                    in.forEach(jobMessenger::send);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            infoThread.setDaemon(true);

            Thread errorThread = new Thread(() -> {
                //コマンド実行時のエラー出力をログに吐き出す
                try (Stream<LogMessage> in = new BufferedReader(new InputStreamReader(start.getErrorStream(), charset))
                    .lines()
                    .peek(line -> lines.add(new TestLogModel.Line(line, TestLogModel.OutputType.ERROR)))
                    .map(LogMessage::new)) {
                    in.forEach(jobMessenger::send);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            errorThread.setDaemon(true);

            infoThread.start();
            errorThread.start();
            //コマンドの実行が終了するまでここでThreadをブロックする
            int waitFor = start.waitFor();

            //次のタスクで使うためにテストログを保存
            TestLogModel testLogModel = new TestLogModel(lines);

            args.putData(TEST_LOG_KEY, testLogModel);
            args.putData(TEST_STATUS_KEY, waitFor == 0 ? TestStatus.PASSING : TestStatus.FAILED);

            return waitFor;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
