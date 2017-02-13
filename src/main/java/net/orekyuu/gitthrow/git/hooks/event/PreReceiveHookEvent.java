package net.orekyuu.gitthrow.git.hooks.event;

import net.orekyuu.gitthrow.project.domain.model.Project;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceivePack;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;

public class PreReceiveHookEvent extends ApplicationEvent {
    private final ReceivePack receivePack;
    private final Collection<ReceiveCommand> commands;
    private final Project project;

    public PreReceiveHookEvent(Object source, ReceivePack receivePack, Collection<ReceiveCommand> commands, Project project) {
        super(source);
        this.receivePack = receivePack;
        this.commands = commands;
        this.project = project;
    }

    public ReceivePack getReceivePack() {
        return receivePack;
    }

    public Collection<ReceiveCommand> getCommands() {
        return commands;
    }

    public Project getProject() {
        return project;
    }
}
