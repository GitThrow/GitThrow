package net.orekyuu.gitthrow.pullrequest.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface PullRequestCommentDao {
    @Insert
    Result<PullRequestCommentTable> insert(PullRequestCommentTable comment);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProjectAndPrNum(String projectId, int prNum, Collector<PullRequestCommentTable, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Delete
    Result<PullRequestCommentTable> delete(PullRequestCommentTable comment);
}
