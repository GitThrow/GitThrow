package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.ProjectUser;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface ProjectUserDao {
    @Insert
    Result<ProjectUser> insert(ProjectUser projectUser) throws DuplicateKeyException;

    @Delete
    Result<ProjectUser> delete(ProjectUser projectUser);

    @BatchDelete
    BatchResult<ProjectUser> delete(Iterable<ProjectUser> projectUser);

    @Select
    Optional<ProjectUser> findByUserAndProject(String projectId, String userId);
}
