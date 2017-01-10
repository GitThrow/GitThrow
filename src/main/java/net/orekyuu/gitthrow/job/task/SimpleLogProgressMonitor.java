package net.orekyuu.gitthrow.job.task;

import net.orekyuu.gitthrow.job.JobMessenger;
import net.orekyuu.gitthrow.job.message.LogMessage;
import org.eclipse.jgit.lib.ProgressMonitor;

class SimpleLogProgressMonitor implements ProgressMonitor {

    private final JobMessenger messenger;

    SimpleLogProgressMonitor(JobMessenger messenger) {
        this.messenger = messenger;
    }

    @Override
    public void start(int i) {

    }

    @Override
    public void beginTask(String s, int i) {
        messenger.send(new LogMessage(s));
    }

    @Override
    public void update(int i) {

    }

    @Override
    public void endTask() {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
