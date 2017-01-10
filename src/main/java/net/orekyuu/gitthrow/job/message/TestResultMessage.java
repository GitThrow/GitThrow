package net.orekyuu.gitthrow.job.message;

public class TestResultMessage implements JobMessage {
    private String jobId;
    private String testResult;

    public TestResultMessage(TestResult testResult) {
        this.testResult = testResult.getValue();
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
        return "testresult";
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}
