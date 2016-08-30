package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;

public interface Task {

    /**
     * タスクが行うべき仕事
     * @return 次のタスクを実行するならtrue
     */
    boolean process(JobMessenger messenger, TaskArguments args) throws Exception;
}
