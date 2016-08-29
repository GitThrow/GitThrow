package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;

public interface Task {

    /**
     * タスクが行うべき仕事
     */
    void process(JobMessenger messenger, TaskArguments args) throws Exception;
}
