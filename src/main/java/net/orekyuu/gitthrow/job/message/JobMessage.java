package net.orekyuu.gitthrow.job.message;


public interface JobMessage {

    void setJobId(String jobId);

    String getJobId();

    String getMessageType();
}
