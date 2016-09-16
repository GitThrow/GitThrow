package net.orekyuu.workbench.job;

import net.orekyuu.workbench.job.message.JobMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public final class JobMessenger {

    private final SseEmitter emitter;
    private final Consumer<Exception> exceptionConsumer;
    private static final Logger logger = LoggerFactory.getLogger(JobMessenger.class);
    private final UUID jobId;

    //SseEmitterの接続が続いている状態か
    private boolean isOpen = true;

    JobMessenger(SseEmitter emitter, UUID jobId, Consumer<Exception> exceptionConsumer) {
        Objects.requireNonNull(jobId);

        this.emitter = emitter;
        this.exceptionConsumer = exceptionConsumer;
        this.jobId = jobId;
        //やり取りが終わったあと
        emitter.onCompletion(() -> {
            logger.info(String.format("[%s] onComplete", jobId.toString()));
            isOpen = false;
        });
    }

    /**
     * クライアントへjson形式でメッセージを送信します
     * @param message 送信するメッセージ
     */
    public void send(JobMessage message) {
        message.setJobId(jobId.toString());
        logger.info(String.format("[%s] start: %s", jobId.toString(), Objects.toString(message, "null")));
        try {
            if (isOpen && emitter != null) {
                emitter.send(message, MediaType.APPLICATION_JSON);
            }
        } catch (IOException | IllegalStateException e) {
            isOpen = false;
            logger.info(String.format("[%s] disconnect", jobId.toString()));
            exceptionConsumer.accept(e);
            if (emitter != null) {
                emitter.completeWithError(e);
            }
        }
    }
}
