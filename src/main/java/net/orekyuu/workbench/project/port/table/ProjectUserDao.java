package net.orekyuu.workbench.project.port.table;

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
    Result<ProjectUserTable> insert(ProjectUserTable projectUserTable) throws DuplicateKeyException;

    @Delete
    Result<ProjectUserTable> delete(ProjectUserTable projectUserTable);

    @BatchDelete
    BatchResult<ProjectUserTable> delete(Iterable<ProjectUserTable> projectUser);

    @Select
    Optional<ProjectUserTable> findByUserAndProject(String projectId, String userId);
}
