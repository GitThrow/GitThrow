package net.orekyuu.workbench.pullrequest.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface PullRequestNumDao {
    @Insert
    Result<PullRequestNumberTable> insert(PullRequestNumberTable num);

    @Update
    Result<PullRequestNumberTable> update(PullRequestNumberTable num);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Select
    Optional<PullRequestNumberTable> findByProjectId(String projectId);
}
