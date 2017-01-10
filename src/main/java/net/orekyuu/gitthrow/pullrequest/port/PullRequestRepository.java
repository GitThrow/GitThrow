package net.orekyuu.gitthrow.pullrequest.port;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.project.usecase.ProjectUsecase;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequest;
import net.orekyuu.gitthrow.pullrequest.domain.model.PullRequestState;
import net.orekyuu.gitthrow.pullrequest.port.table.*;
import net.orekyuu.gitthrow.pullrequest.util.PullRequestUtil;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.user.usecase.UserUsecase;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PullRequestRepository {

    private final ClosedPullRequestDao closedPullRequestDao;
    private final OpenPullRequestDao openPullRequestDao;
    private final PullRequestNumDao pullRequestNumDao;
    private final UserUsecase userUsecase;

    private final ProjectUsecase projectUsecase;

    public PullRequestRepository(ClosedPullRequestDao closedPullRequestDao, OpenPullRequestDao openPullRequestDao, PullRequestNumDao pullRequestNumDao, UserUsecase userUsecase, ProjectUsecase projectUsecase) {
        this.closedPullRequestDao = closedPullRequestDao;
        this.openPullRequestDao = openPullRequestDao;
        this.pullRequestNumDao = pullRequestNumDao;
        this.userUsecase = userUsecase;
        this.projectUsecase = projectUsecase;
    }

    public PullRequest create(Project project, String title, String desc, User reviewer, User proponent, String baseBranch, String targetBranch) {
        if (!projectUsecase.isJoined(project.getId(), reviewer.getId())) {
            throw new IllegalArgumentException("reviewerがプロジェクトメンバーではありません");
        }

        if (!projectUsecase.isJoined(project.getId(), proponent.getId())) {
            throw new IllegalArgumentException("proponentがプロジェクトメンバーではありません");
        }

        //番号を更新
        PullRequestNumberTable numberTable = pullRequestNumDao.findByProjectId(project.getId()).get();
        numberTable = pullRequestNumDao.update(new PullRequestNumberTable(project.getId(), numberTable.getPrCount() + 1)).getEntity();

        OpenPullRequestTable result = openPullRequestDao.insert(new OpenPullRequestTable(
            project.getId(),
            numberTable.getPrCount(),
            title,
            desc,
            reviewer.getId(),
            proponent.getId(),
            baseBranch,
            targetBranch)).getEntity();
        return PullRequestUtil.fromTable(result, reviewer, proponent);
    }

    public PullRequest save(PullRequest pullRequest) {
        if (!projectUsecase.isJoined(pullRequest.getProjectId(), pullRequest.getReviewer().getId())) {
            throw new IllegalArgumentException("reviewerがプロジェクトメンバーではありません");
        }

        if (!projectUsecase.isJoined(pullRequest.getProjectId(), pullRequest.getProponent().getId())) {
            throw new IllegalArgumentException("proponentがプロジェクトメンバーではありません");
        }

        if (pullRequest.getState() == PullRequestState.OPEN) {
            OpenPullRequestTable result = openPullRequestDao.update(new OpenPullRequestTable(
                pullRequest.getProjectId(),
                (long) pullRequest.getPullrequestNum(),
                pullRequest.getTitle(),
                pullRequest.getDescription(),
                pullRequest.getReviewer().getId(),
                pullRequest.getProponent().getId(),
                pullRequest.getBase(),
                pullRequest.getTarget()
            )).getEntity();
            return PullRequestUtil.fromTable(result, pullRequest.getReviewer(), pullRequest.getProponent());
        }
        if (pullRequest.getState() == PullRequestState.MERGED) {
            ClosedPullRequestTable result = closedPullRequestDao.update(new ClosedPullRequestTable(
                pullRequest.getProjectId(),
                (long) pullRequest.getPullrequestNum(),
                pullRequest.getTitle(),
                pullRequest.getDescription(),
                pullRequest.getReviewer().getId(),
                pullRequest.getProponent().getId(),
                pullRequest.getBase(),
                pullRequest.getTarget()
            )).getEntity();
            return PullRequestUtil.fromTable(result, pullRequest.getReviewer(), pullRequest.getProponent());
        }
        throw new IllegalArgumentException("PullRequest state do not match: " + pullRequest.getState());
    }

    public PullRequest merge(PullRequest pullrequest, String baseCommit, String targetCommit) {
        if (pullrequest.getState() != PullRequestState.OPEN) {
            throw new IllegalStateException("PullRequest is not open: " + pullrequest.getState());
        }

        openPullRequestDao.delete(pullrequest.getProjectId(), pullrequest.getPullrequestNum());

        ClosedPullRequestTable result = closedPullRequestDao.insert(new ClosedPullRequestTable(
            pullrequest.getProjectId(),
            (long)pullrequest.getPullrequestNum(),
            pullrequest.getTitle(),
            pullrequest.getDescription(),
            pullrequest.getReviewer().getId(),
            pullrequest.getProponent().getId(),
            baseCommit,
            targetCommit
        )).getEntity();

        return PullRequestUtil.fromTable(
            result,
            userUsecase.findById(pullrequest.getReviewer().getId()).get(),
            userUsecase.findById(pullrequest.getProponent().getId()).get());
    }

    public List<PullRequest> findByProject(Project project) {
        HashMap<String, User> userMap = new HashMap<>();

        List<PullRequest> open = openPullRequestDao.findByProject(project.getId(),
            Collectors.mapping(table -> PullRequestUtil.fromTable(
            table,
            userMap.computeIfAbsent(table.getReviewer(), id -> userUsecase.findById(id).get()),
            userMap.computeIfAbsent(table.getProponent(), id -> userUsecase.findById(id).get())
        ), Collectors.toList()));

        List<PullRequest> merged = closedPullRequestDao.findByProject(project.getId(), Collectors.mapping(table -> PullRequestUtil.fromTable(
            table,
            userMap.computeIfAbsent(table.getReviewer(), id -> userUsecase.findById(id).get()),
            userMap.computeIfAbsent(table.getProponent(), id -> userUsecase.findById(id).get())
        ), Collectors.toList()));

        open.addAll(merged);

        Collections.sort(open, Comparator.comparingInt(PullRequest::getPullrequestNum));

        return open;
    }

    public Optional<PullRequest> findById(Project project, int num) {
        Optional<PullRequest> pr = openPullRequestDao.findByProjectAndNum(project.getId(), num)
            .map(it -> PullRequestUtil.fromTable(
                it,
                userUsecase.findById(it.getReviewer()).get(),
                userUsecase.findById(it.getProponent()).get()));

        if (pr.isPresent()) {
            return pr;
        }

        return closedPullRequestDao.findByProjectAndNum(project.getId(), num)
            .map(it -> PullRequestUtil.fromTable(
                it,
                userUsecase.findById(it.getReviewer()).get(),
                userUsecase.findById(it.getProponent()).get()));
    }

    public void deleteByProject(Project project) {
        openPullRequestDao.deleteByProject(project.getId());
        closedPullRequestDao.deleteByProject(project.getId());
    }
}
