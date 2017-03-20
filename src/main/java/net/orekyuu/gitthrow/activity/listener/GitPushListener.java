package net.orekyuu.gitthrow.activity.listener;

import net.orekyuu.gitthrow.activity.usecase.ActivityUsecase;
import net.orekyuu.gitthrow.git.domain.RemoteRepository;
import net.orekyuu.gitthrow.git.domain.RemoteRepositoryFactory;
import net.orekyuu.gitthrow.git.hooks.event.PostReceiveHookEvent;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import net.orekyuu.gitthrow.user.util.BotUserUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.ArrayList;

@Component
@ComponentScan
public class GitPushListener implements ApplicationListener<PostReceiveHookEvent> {

    @Autowired
    private ActivityUsecase usecase;
    @Autowired
    private UserUsecase userUsecase;

    @Autowired
    private RemoteRepositoryFactory factory;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void onApplicationEvent(PostReceiveHookEvent event) {
        transactionTemplate.setReadOnly(false);
        transactionTemplate.execute((TransactionCallback<Void>) status -> {
            onPostReceiceHook(event);
            return null;
        });
    }

    private void onPostReceiceHook(PostReceiveHookEvent event) {
        RemoteRepository repository = factory.create(event.getProject().getId());
        for (ReceiveCommand command : event.getCommands()) {
            //CREATEとUPDATE以外はActivityに残したくないので弾く
            if (command.getType() != ReceiveCommand.Type.CREATE && command.getType() != ReceiveCommand.Type.UPDATE) {
                continue;
            }

            if (command.getResult() != ReceiveCommand.Result.OK) {
                continue;
            }
            String branch = command.getRefName();
            ArrayList<String> logs = new ArrayList<>();

            try (Git git = new Git(repository.getRepository())) {
                Iterable<RevCommit> commits = git.log().addRange(command.getOldId(), command.getNewId()).call();
                for (RevCommit commit : commits) {
                    PersonIdent committer = commit.getCommitterIdent();
                    String name = committer.getName();
                    String emailAddress = committer.getEmailAddress();
                    String shortMessage = commit.getShortMessage();
                    logs.add(String.format("%s(%s) %s  %s", name, emailAddress, commit.getId().abbreviate(7).name(), shortMessage));
                }
            } catch (IOException | GitAPIException e) {
                e.printStackTrace();
            }
            String botUserId = BotUserUtil.toBotUserId(event.getProject().getId());
            userUsecase.findById(botUserId).ifPresent(bot -> {
                usecase.createPushActivity(event.getProject(), branch, logs, bot);
            });
        }
    }
}
