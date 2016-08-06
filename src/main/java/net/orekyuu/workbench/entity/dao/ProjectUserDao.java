package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.ProjectUser;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;
import org.springframework.dao.DuplicateKeyException;

@ConfigAutowireable
@Dao
public interface ProjectUserDao {
    @Insert
    Result<ProjectUser> insert(ProjectUser projectUser) throws DuplicateKeyException;

    @Delete
    Result<ProjectUser> delete(ProjectUser projectUser);

    @BatchDelete
    BatchResult<ProjectUser> delete(Iterable<ProjectUser> projectUser);
}
