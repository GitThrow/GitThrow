package net.orekyuu.gitthrow.git;

import net.orekyuu.gitthrow.git.domain.RemoteRepository;
import net.orekyuu.gitthrow.git.domain.RemoteRepositoryFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Component
public class FileDiffSender {
    @Autowired
    private RemoteRepositoryFactory repositoryFactory;

    @Async
    public void calcDiff(String projectId, String base, String target, SseEmitter emitter) throws GitAPIException, IOException {
        RemoteRepository repository = repositoryFactory.create(projectId);

        List<DiffEntry> diff = repository.diff(base, target);
        for (DiffEntry entry : diff) {
            FileDiff fileDiff = repository.calcFileDiff(entry);
            emitter.send(fileDiff);
        }
        //なんかcompleteができてなくてクライアント側でエラー？
        emitter.complete();
    }
}
