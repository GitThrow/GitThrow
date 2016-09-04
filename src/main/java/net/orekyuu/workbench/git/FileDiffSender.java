package net.orekyuu.workbench.git;

import net.orekyuu.workbench.service.RemoteRepositoryService;
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
    private RemoteRepositoryService remoteRepositoryService;

    @Async
    public void calcDiff(String projectId, String base, String target, SseEmitter emitter) throws GitAPIException, IOException {
        List<DiffEntry> diff = remoteRepositoryService.diff(projectId, base, target);
        for (DiffEntry entry : diff) {
            FileDiff fileDiff = remoteRepositoryService.calcFileDiff(projectId, entry);
            emitter.send(fileDiff);
        }
        //なんかcompleteができてなくてクライアント側でエラー？
        emitter.complete();
    }
}
