package net.orekyuu.workbench.project.port;

import net.orekyuu.workbench.git.domain.RemoteRepository;
import net.orekyuu.workbench.git.domain.RemoteRepositoryFactory;
import net.orekyuu.workbench.project.domain.model.Project;
import net.orekyuu.workbench.project.port.table.ProjectDao;
import net.orekyuu.workbench.project.port.table.ProjectTable;
import net.orekyuu.workbench.project.port.table.ProjectUserDao;
import net.orekyuu.workbench.project.port.table.ProjectUserTable;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import net.orekyuu.workbench.ticket.port.table.*;
import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.port.UserRepository;
import net.orekyuu.workbench.user.port.table.*;
import net.orekyuu.workbench.user.util.BotUserUtil;
import net.orekyuu.workbench.user.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProjectRepository {

    private final ProjectDao projectDao;
    private final ProjectUserDao projectUserDao;
    private final TicketNumDao ticketNumDao;
    private final TicketPriorityDao ticketPriorityDao;
    private final TicketStatusDao ticketStatusDao;
    private final TicketTypeDao ticketTypeDao;
    private final RemoteRepositoryFactory repositoryFactory;
    private final UserAvatarDao userAvatarDao;
    private final UserSettingDao userSettingDao;
    private final UserDao userDao;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(ProjectRepository.class);

    private static final List<String> defaultTicketStatus = Arrays.asList("新規", "進行中", "完了", "保留");
    private static final List<String> defaultTicketPriority = Arrays.asList("低", "中", "高");
    private static final List<String> defaultTicketType = Arrays.asList("バグ", "新規機能", "提案", "リリース", "etc");

    public ProjectRepository(ProjectDao projectDao, ProjectUserDao projectUserDao, TicketNumDao ticketNumDao, TicketPriorityDao ticketPriorityDao, TicketStatusDao ticketStatusDao, TicketTypeDao ticketTypeDao, RemoteRepositoryFactory repositoryFactory, UserAvatarDao userAvatarDao, UserSettingDao userSettingDao, UserDao userDao, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.projectDao = projectDao;
        this.userDao = userDao;
        this.userRepository = userRepository;
        this.projectUserDao = projectUserDao;
        this.ticketNumDao = ticketNumDao;
        this.ticketPriorityDao = ticketPriorityDao;
        this.ticketStatusDao = ticketStatusDao;
        this.ticketTypeDao = ticketTypeDao;
        this.repositoryFactory = repositoryFactory;
        this.userAvatarDao = userAvatarDao;
        this.userSettingDao = userSettingDao;
        this.passwordEncoder = passwordEncoder;
    }

    private Project fromTable(ProjectTable table) {
        Objects.requireNonNull(table, "project table is null.");
        User user = userRepository.findById(table.getOwnerUserId())
            .orElseThrow(() -> new UsernameNotFoundException("owner is not found."));

        List<User> member = findMember(table.getId());
        return new Project(table.getId(), table.getName(), user, member);
    }

    /**
     * プロジェクトを作成します
     * @param projectId プロジェクトID
     * @param projectName プロジェクト名
     * @param owner プロジェクトの管理者
     * @return 作成したプロジェクト
     * @throws IOException
     * @throws UserExistsException
     */
    public Project create(String projectId, String projectName, User owner) throws IOException, UserExistsException {
        ProjectTable table = new ProjectTable(projectId, projectName, owner.getId());
        Project result = fromTable(projectDao.insert(table).getEntity());

        RemoteRepository repository = repositoryFactory.create(projectId);
        repository.init();

        ticketNumDao.insert(new TicketNumTable(projectId, 0L));
        //デフォルトの優先度一覧
        defaultTicketPriority.stream()
            .map(str -> new TicketPriorityTable(null, projectId, str))
            .forEach(ticketPriorityDao::insert);

        //デフォルトの状態一覧
        defaultTicketStatus.stream()
            .map(str -> new TicketStatusTable(null, projectId, str))
            .forEach(ticketStatusDao::insert);

        //デフォルトのタイプ一覧
        defaultTicketType.stream()
            .map(str -> new TicketTypeTable(null, projectId, str))
            .forEach(ticketTypeDao::insert);

        createBot(projectId);
        projectUserDao.insert(new ProjectUserTable(projectId, owner.getId()));

        logger.info("Project created: " + result.toString());
        return result;
    }

    /**
     * プロジェクトの情報を保存します
     * @param project 保存するプロジェクト
     * @return 更新後のプロジェクト
     */
    public Project save(Project project) {
        ProjectTable table = new ProjectTable(project.getId(), project.getName(), project.getOwner().getId());
        Project result = fromTable(projectDao.update(table).getEntity());

        logger.info("Project updated: " + result.toString());
        return result;
    }

    /**
     * プロジェクトを削除します
     * @param project 削除するプロジェクト
     * @return 削除したプロジェクト
     */
    public Project delete(Project project) {
        ProjectTable table = new ProjectTable(project.getId(), project.getName(), project.getOwner().getId());
        Project result = fromTable(projectDao.delete(table).getEntity());

        ticketNumDao.deleteByProject(project.getId());
        ticketPriorityDao.deleteByProject(project.getId());
        ticketStatusDao.deleteByProject(project.getId());
        ticketTypeDao.deleteByProject(project.getId());

        logger.info("Project deleted: " + result);
        return result;
    }

    /**
     * プロジェクトIDからプロジェクトを検索します
     * @param projectId プロジェクトID
     * @return 見つかったプロジェクト
     */
    public Optional<Project> findById(String projectId) {
        return projectDao.findById(projectId).map(this::fromTable);
    }

    /**
     * 与えられたユーザーが参加しているプロジェクトのリストを返します
     * @param user ユーザー
     * @return 引数のユーザーが参加しているプロジェクト
     */
    public List<Project> findByUser(User user) {
        return projectDao.findByUser(user.getId(), projectStream ->
            projectStream
                .map(this::fromTable)
                .collect(Collectors.toList()));
    }

    //メンバーの取得はProjectからしか操作できないようにする
    private List<User> findMember(String projectId) {
        return projectDao.findProjectMember(projectId, stream ->
            stream
                .map(UserUtil::fromTable)
                .collect(Collectors.toList()));
    }

    private User createBot(String projectId) throws UserExistsException {
        String botId = BotUserUtil.toBotUserId(projectId);
        UserTable bot = new UserTable(botId, "ボット", null, false);

        try {
            userDao.insert(new UserTable(bot.getId(), bot.getName(), passwordEncoder.encode("password"), bot.getEmail(), bot.isAdmin()));
        } catch (DuplicateKeyException e) {
            //ユーザーが存在してるので失敗
            throw new UserExistsException(botId, e);
        }

        //アバター
        UserAvatarTable avatar = new UserAvatarTable(bot.getId(), UserUtil.loadDefaultUserIcon());
        try {
            userAvatarDao.insert(avatar);
        } catch (DuplicateKeyException e) {
            //ユーザーが存在してるので失敗
            throw new UserExistsException(bot.getId(), e);
        }

        //設定
        UserSettingTable setting = new UserSettingTable(bot.getId(), false);
        try {
            userSettingDao.insert(setting);
        } catch (DuplicateKeyException e) {
            //ユーザーが存在してるので失敗
            throw new UserExistsException(bot.getId(), e);
        }
        return UserUtil.fromTable(bot);
    }

}
