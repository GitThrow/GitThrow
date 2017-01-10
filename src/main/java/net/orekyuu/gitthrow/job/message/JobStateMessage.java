package net.orekyuu.gitthrow.job.message;

/**
 * ジョブの状態を通知するメッセージ
 */
public class JobStateMessage implements JobMessage {

    private String jobId;
    private String state;
    private String message;

    public JobStateMessage(JobState state) {
        this(state, null);
    }

    public JobStateMessage(JobState state, String message) {
        this.state = state.getStateName();
        this.message = message == null ? "" : message;
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
        return "state";
    }

    public String getState() {
        return state;
    }
}
