package net.orekyuu.workbench.project.usecase;

import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.project.domain.policy.ProjectMemberPolicy;
import net.orekyuu.workbench.project.port.ProjectRepository;
import net.orekyuu.workbench.project.port.table.ProjectUserDao;
import net.orekyuu.workbench.project.port.table.ProjectUserTable;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.util.PolicyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProjectUsecase {


    private final ProjectRepository projectRepository;
    private final ProjectUserDao projectUserDao;

    public ProjectUsecase(ProjectRepository projectRepository, ProjectUserDao projectUserDao) {
        this.projectRepository = projectRepository;
        this.projectUserDao = projectUserDao;
    }


    @Transactional(readOnly = false)
    public Project createProject(String projectId, String projectName, User owner) throws ProjectExistsException, IOException, UserExistsException {
        return projectRepository.create(projectId, projectName, owner);
    }

    @Transactional(readOnly = false)
    public Project join(Project project, User user) {
        List<User> member = project.getMember();
        if (member.stream().noneMatch(it -> it.getId().equals(user.getId()))) {
            if (!new ProjectMemberPolicy(project).check(user)) {
                throw new PolicyException();
            }
            projectUserDao.insert(new ProjectUserTable(project.getId(), user.getId()));
            member.add(user);
            return new Project(project.getId(), project.getName(), project.getOwner(), member);
        }
        return project;
    }

    @Transactional(readOnly = false)
    public Project withdraw(Project project, User user) {
        List<User> member = project.getMember();
        if (member.stream().anyMatch(it -> it.getId().equals(user.getId()))) {
            if (!new ProjectMemberPolicy(project).check(user)) {
                throw new PolicyException();
            }
            projectUserDao.delete(new ProjectUserTable(project.getId(), user.getId()));
            member.remove(user);
            return new Project(project.getId(), project.getName(), project.getOwner(), member);
        }
        return project;
    }

    @Transactional(readOnly = false)
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Transactional(readOnly = false)
    public Project delete(Project project) {
        return projectRepository.delete(project);
    }

    public Optional<Project> findById(String projectId) {
        return projectRepository.findById(projectId);
    }

    public List<Project> findByUser(User user) {
        return projectRepository.findByUser(user);
    }

    public boolean isJoined(String projectId, String userId) {
        return projectUserDao.findByUserAndProject(projectId, userId).isPresent();
    }
}
