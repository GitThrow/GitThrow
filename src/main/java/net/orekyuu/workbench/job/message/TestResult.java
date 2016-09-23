package net.orekyuu.workbench.job.message;

public enum TestResult {
    PASSING("passing"),
    FAILED("failed");

    private final String value;

    TestResult(String str) {
        this.value = str;
    }

    public String getValue() {
        return value;
    }
}
