package net.orekyuu.gitthrow.job.message;

public enum JobState {

    /**
     * 開始された状態
     */
    START("start"),
    /**
     * 正常終了
     */
    SUCCESS("success"),
    /**
     * 以上終了
     */
    ERROR("error");

    private final String state;

    JobState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return state;
    }
}
