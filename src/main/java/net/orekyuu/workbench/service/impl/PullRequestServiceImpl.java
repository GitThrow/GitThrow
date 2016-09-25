package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.entity.ClosedPullRequest;
import net.orekyuu.workbench.entity.OpenPullRequest;
import net.orekyuu.workbench.entity.dao.ClosedPullRequestDao;
import net.orekyuu.workbench.entity.dao.OpenPullRequestDao;
import net.orekyuu.workbench.entity.dao.TicketNumDao;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.PullRequestModel;
import net.orekyuu.workbench.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class PullRequestServiceImpl implements PullRequestService {

    @Autowired
    private OpenPullRequestDao openPullRequestDao;
    @Autowired
    private ClosedPullRequestDao closedPullRequestDao;

    @Autowired
    private TicketNumDao ticketNumDao;
    @Autowired
    private ProjectService projectService;

    @Transactional(readOnly = false)
    @Override
    public void create(OpenPullRequest pullRequest) {
        if (!projectService.isJoined(pullRequest.project, pullRequest.reviewer)) {
            throw new NotMemberException();
        }

        if (!projectService.isJoined(pullRequest.project, pullRequest.proponent)) {
            throw new NotMemberException();
        }

        ticketNumDao.increment(pullRequest.project);
        openPullRequestDao.insert(pullRequest);
    }

    @Override
    public List<PullRequestModel> findByProject(String projectId) {
        LinkedList<PullRequestModel> list = new LinkedList<>();

        List<OpenPullRequest> openPR = openPullRequestDao.findByProject(projectId, Collectors.toList());
        list.addAll(openPR.stream()
            .map(this::mapToModel)
            .collect(Collectors.toList())
        );
        List<ClosedPullRequest> closedPR = closedPullRequestDao.findByProject(projectId, Collectors.toList());
        list.addAll(closedPR.stream()
            .map(this::mapToModel)
            .collect(Collectors.toList())
        );

        //PR番号でソート
        list.sort(Comparator.comparingInt(PullRequestModel::getNumber));
        return list;
    }

    @Override
    public Optional<PullRequestModel> findByProjectAndNum(String projectId, int prNum) {
        Optional<PullRequestModel> opt = openPullRequestDao.findByProjectAndNum(projectId, prNum).map(this::mapToModel);
        if (opt.isPresent()) {
            return opt;
        }
        return closedPullRequestDao.findByProjectAndNum(projectId, prNum).map(this::mapToModel);
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteByProject(String projectId) {
        openPullRequestDao.deleteByProject(projectId);
        closedPullRequestDao.deleteByProject(projectId);
    }

    @Transactional(readOnly = false)
    @Override
    public void close(String projectId, int prNum, String baseCommit, String targetCommit) {
        OpenPullRequest pr = openPullRequestDao.findByProjectAndNum(projectId, prNum).orElseThrow(RuntimeException::new);
        ClosedPullRequest closedPullRequest = toClosedPullRequest(pr, baseCommit, targetCommit);
        closedPullRequestDao.insert(closedPullRequest);
        openPullRequestDao.delete(pr.project, pr.prNum);
    }

    private PullRequestModel mapToModel(OpenPullRequest pr) {
        return new PullRequestModel(pr.prNum, pr.title, pr.description, pr.reviewer, pr.proponent, pr.baseBranch, pr.targetBranch, false);
    }

    private PullRequestModel mapToModel(ClosedPullRequest pr) {
        return new PullRequestModel(pr.prNum, pr.title, pr.description, pr.reviewer, pr.proponent, pr.baseCommit, pr.targetCommit, true);
    }


    private ClosedPullRequest toClosedPullRequest(OpenPullRequest pr, String baseCommit, String targetCommit) {
        ClosedPullRequest pullRequest = new ClosedPullRequest();
        pullRequest.project = pr.project;
        pullRequest.prNum = pr.prNum;
        pullRequest.title = pr.title;
        pullRequest.description = pr.description;
        pullRequest.proponent = pr.proponent;
        pullRequest.reviewer = pr.reviewer;
        pullRequest.baseCommit = baseCommit;
        pullRequest.targetCommit = targetCommit;
        return pullRequest;
    }
}
