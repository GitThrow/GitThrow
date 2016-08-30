package net.orekyuu.workbench.job.message;


/**
 * ログの情報を送るためのメッセージ
 */
public class LogMessage implements JobMessage {

    /**
     * ジョブID
     */
    private String jobId;
    /**
     * 出力内容
     */
    private String message;

    public LogMessage(String message) {
        this.message = message;
    }

    @Override
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String getJobId() {
        return jobId;
    }

    @Override
    public String getMessageType() {
        return "log";
    }

    public String getMessage() {
        return message;
    }
}
