package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.ClosedPullRequest;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface ClosedPullRequestDao {

    @Insert
    int insert(ClosedPullRequest pr);

    @Update(sqlFile = true)
    int update(ClosedPullRequest pr);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Select
    Optional<ClosedPullRequest> findByProjectAndNum(String projectId, int prNum);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<ClosedPullRequest, ?, RESULT> collector);
}
