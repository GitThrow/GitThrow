package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.OpenPullRequest;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface OpenPullRequestDao {

    @Insert(sqlFile = true)
    int insert(OpenPullRequest pr);

    @Update(sqlFile = true)
    int update(OpenPullRequest pr);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Delete(sqlFile = true)
    int delete(String projectId, int prNum);

    @Select
    Optional<OpenPullRequest> findByProjectAndNum(String projectId, int prNum);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<OpenPullRequest, ?, RESULT> collector);
}
