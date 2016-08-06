package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.exceptions.NotProjectMemberException;
import net.orekyuu.workbench.service.exceptions.ProjectExistsException;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProjectService {

    /**
     * プロジェクトを作成します
     * @param projectId プロジェクトのID
     * @param projectName プロジェクトの表示名
     * @param owner プロジェクトの管理者
     */
    void createProject(String projectId, String projectName, User owner) throws ProjectExistsException;

    /**
     * プロジェクトに参加する<br/>
     * すでに参加している場合は何もしない
     * @param projectId プロジェクトID
     * @param userId ユーザーID
     */
    void joinToProject(String projectId, String userId);

    /**
     * プロジェクトから脱退する<br/>
     * もともと参加していない場合は何もしない
     * @param projectId プロジェクトID
     * @param userId ユーザーID
     */
    void withdrawProject(String projectId, String userId);

    /**
     * プロジェクトメンバーを検索します
     * @param projectId プロジェクトID
     * @return プロジェクトに参加しているメンバー
     */
    List<User> findProjectMember(String projectId) throws ProjectNotFoundException;

    /**
     * プロジェクト名を更新します
     * @param projectId プロジェクトID
     * @param projectName 新しいプロジェクト名
     */
    void updateProjectName(String projectId, String projectName) throws ProjectNotFoundException;

    /**
     * プロジェクトの管理者を変更します
     * @param projectId プロジェクトID
     * @param newProjectOwnerId 新しいプロジェクトの管理者のユーザーID
     */
    void changeProjectOwner(String projectId, String newProjectOwnerId) throws ProjectNotFoundException, NotProjectMemberException;

    /**
     * プロジェクトを検索します
     * @param projectId 検索するプロジェクトID
     * @return 検索結果
     */
    Optional<Project> findById(String projectId);

    /**
     * プロジェクトを削除します
     * @param projectId プロジェクトID
     */
    void deleteProject(String projectId) throws ProjectNotFoundException;
}
