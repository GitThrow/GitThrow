package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.OpenPullRequest;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface OpenPullRequestDao {

    @Insert(sqlFile = true)
    Result<OpenPullRequest> insert(OpenPullRequest pr);

    @Update(sqlFile = true)
    Result<OpenPullRequest> update(OpenPullRequest pr);

    @Delete(sqlFile = true)
    Result<OpenPullRequest> deleteByProject(String projectId);

    @Delete(sqlFile = true)
    Result<OpenPullRequest> delete(String projectId, int prNum);

    @Select
    Optional<OpenPullRequest> findByProjectAndNum(String projectId, int prNum);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<OpenPullRequest, ?, RESULT> collector);
}
