package net.orekyuu.workbench.pullrequest.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface OpenPullRequestDao {

    @Insert(sqlFile = true)
    Result<OpenPullRequestTable> insert(OpenPullRequestTable pr);

    @Update(sqlFile = true)
    Result<OpenPullRequestTable> update(OpenPullRequestTable pr);

    @Delete(sqlFile = true)
    Result<OpenPullRequestTable> deleteByProject(String projectId);

    @Delete(sqlFile = true)
    Result<OpenPullRequestTable> delete(String projectId, int prNum);

    @Select
    Optional<OpenPullRequestTable> findByProjectAndNum(String projectId, int prNum);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<OpenPullRequestTable, ?, RESULT> collector);
}
