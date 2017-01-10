package net.orekyuu.gitthrow.job.message;

public enum  BuildResult {
    SUCCESS("success"),
    FAILED("failed");

    private final String value;

    BuildResult(String str) {
        this.value = str;
    }

    public String getValue() {
        return value;
    }
}
