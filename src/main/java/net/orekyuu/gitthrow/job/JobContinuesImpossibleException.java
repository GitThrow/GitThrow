package net.orekyuu.gitthrow.job;

/**
 * ジョブが続行不能なときに投げられる例外。
 * ジョブの実行結果は{@link net.orekyuu.gitthrow.job.message.JobState}のERRORになり、次の続くタスクは全てスキップされて終了します。
 */
public class JobContinuesImpossibleException extends Exception {

    private String errorMessage = "";
    /**
     * @param message クライアントに送信するメッセージ
     */
    public JobContinuesImpossibleException(String message) {
        errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
