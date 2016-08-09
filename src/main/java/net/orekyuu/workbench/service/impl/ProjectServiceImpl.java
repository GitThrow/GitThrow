package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.*;
import net.orekyuu.workbench.entity.dao.*;
import net.orekyuu.workbench.service.ProjectService;
import net.orekyuu.workbench.service.exceptions.NotProjectMemberException;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Component
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ProjectUserDao projectUserDao;
    @Autowired
    private TicketStatusDao ticketStatusDao;
    @Autowired
    private TicketPriorityDao ticketPriorityDao;
    @Autowired
    private TicketTypeDao ticketTypeDao;
    @Autowired
    private TicketNumDao ticketNumDao;

    private static final List<String> defaultTicketStatus = Arrays.asList("新規", "a");
    private static final List<String> defaultTicketPriority = Arrays.asList("低", "中", "高");
    private static final List<String> defaultTicketType = Arrays.asList("バグ", "新規機能", "提案", "リリース", "etc");

    @Transactional(readOnly = false)
    @Override
    public void createProject(String projectId, String projectName, User owner) throws ProjectExistsException {
        try {
            projectDao.insert(new Project(projectId, projectName, owner.id));
            projectUserDao.insert(new ProjectUser(projectId, owner.id));

            TicketNum num = new TicketNum();
            num.ticketCount = 0;
            num.project = projectId;
            ticketNumDao.insert(num);
            //デフォルトの優先度一覧
            defaultTicketPriority.stream().map(str -> {
                TicketPriority priority = new TicketPriority();
                priority.project = projectId;
                priority.priority = str;
                return priority;
            }).forEach(t -> ticketPriorityDao.insert(t));

            //デフォルトの状態一覧
            defaultTicketStatus.stream().map(str -> {
                TicketStatus status = new TicketStatus();
                status.project = projectId;
                status.status = str;
                return status;
            }).forEach(t -> ticketStatusDao.insert(t));

            //デフォルトのタイプ一覧
            defaultTicketType.stream().map(str -> {
                TicketType type = new TicketType();
                type.project = projectId;
                type.type = str;
                return type;
            }).forEach(t -> ticketTypeDao.insert(t));
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            throw new ProjectExistsException(projectId);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void joinToProject(String projectId, String userId) {
        try {
            projectUserDao.insert(new ProjectUser(projectId, userId));
        } catch (DuplicateKeyException e) {
            //すでに存在している場合は何もしない
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void withdrawProject(String projectId, String userId) {
        Optional<Project> optional = projectDao.findById(projectId);
        if (!optional.isPresent()) {
            return;
        }
        Project project = optional.get();
        if (project.ownerUserId.equals(userId)) {
            throw new UnsupportedOperationException("管理者は削除できません");
        }

        projectUserDao.delete(new ProjectUser(projectId, userId));
    }

    @Override
    public List<User> findProjectMember(String projectId) throws ProjectNotFoundException {
        return projectDao.findProjectMember(projectId, Collectors.toList());
    }

    @Override
    public List<Project> findProjectByUser(String userId) {
        return projectDao.findByUser(userId, Collectors.toList());
    }

    @Transactional(readOnly = false)
    @Override
    public void updateProjectName(String projectId, String projectName) throws ProjectNotFoundException {
        Optional<Project> result = projectDao.findById(projectId);
        Project project = result.orElseThrow(() -> new ProjectNotFoundException(projectId));
        project.name = projectName;
        projectDao.update(project);
    }

    @Transactional(readOnly = false)
    @Override
    public void changeProjectOwner(String projectId, String newProjectOwnerId) throws ProjectNotFoundException, NotProjectMemberException {
        List<User> projectMember = findProjectMember(projectId);
        User newOwner = projectMember.stream()
            .filter(user -> user.id.equals(newProjectOwnerId))
            .findFirst()
            .orElseThrow(NotProjectMemberException::new);

        Project project = findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        project.ownerUserId = newOwner.id;
        projectDao.update(project);
    }

    @Override
    public Optional<Project> findById(String projectId) {
        return projectDao.findById(projectId);
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteProject(String projectId) throws ProjectNotFoundException {
        List<User> member = findProjectMember(projectId);
        projectUserDao.delete(member.stream()
            .map(user -> new ProjectUser(projectId, user.id))
            .collect(Collectors.toList())
        );
        projectDao.delete(new Project(projectId, "", ""));
    }
}
