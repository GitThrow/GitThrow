package net.orekyuu.workbench.pullrequest.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface ClosedPullRequestDao {

    @Insert
    Result<ClosedPullRequestTable> insert(ClosedPullRequestTable pr);

    @Update(sqlFile = true)
    Result<ClosedPullRequestTable> update(ClosedPullRequestTable pr);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Select
    Optional<ClosedPullRequestTable> findByProjectAndNum(String projectId, int prNum);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<ClosedPullRequestTable, ?, RESULT> collector);
}
