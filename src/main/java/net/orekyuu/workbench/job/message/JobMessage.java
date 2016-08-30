package net.orekyuu.workbench.job.message;


public interface JobMessage {

    void setJobId(String jobId);

    String getJobId();

    String getMessageType();
}
