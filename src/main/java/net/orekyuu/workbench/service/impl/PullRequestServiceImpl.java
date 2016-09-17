package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.controller.view.user.project.NotMemberException;
import net.orekyuu.workbench.entity.OpenPullRequest;
import net.orekyuu.workbench.entity.dao.OpenPullRequestDao;
import net.orekyuu.workbench.entity.dao.TicketNumDao;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.PullRequestModel;
import net.orekyuu.workbench.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PullRequestServiceImpl implements PullRequestService {

    @Autowired
    private OpenPullRequestDao openPullRequestDao;
    @Autowired
    private TicketNumDao ticketNumDao;
    @Autowired
    private ProjectService projectService;

    @Override
    public void create(OpenPullRequest pullRequest) {
        if (!projectService.isJoined(pullRequest.project, pullRequest.reviewer)) {
            throw new NotMemberException();
        }

        if (!projectService.isJoined(pullRequest.project, pullRequest.proponent)) {
            throw new NotMemberException();
        }

        openPullRequestDao.insert(pullRequest);
        ticketNumDao.increment(pullRequest.project);
    }

    @Override
    public List<PullRequestModel> findByProject(String projectId) {
        LinkedList<PullRequestModel> list = new LinkedList<>();

        List<OpenPullRequest> openPR = openPullRequestDao.findByProject(projectId, Collectors.toList());
        list.addAll(openPR.stream()
            .map(this::mapToModel)
            .collect(Collectors.toList())
        );
        return list;
    }

    @Override
    public Optional<PullRequestModel> findByProjectAndNum(String projectId, int prNum) {
        Optional<PullRequestModel> opt = openPullRequestDao.findByProjectAndNum(projectId, prNum).map(this::mapToModel);
        if (opt.isPresent()) {
            return opt;
        }
        return Optional.empty();
    }

    @Override
    public void deleteByProject(String projectId) {
        openPullRequestDao.deleteByProject(projectId);
    }

    private PullRequestModel mapToModel(OpenPullRequest pr) {
        return new PullRequestModel(pr.prNum, pr.title, pr.description, pr.reviewer, pr.proponent, pr.baseBranch, pr.targetBranch);
    }
}
