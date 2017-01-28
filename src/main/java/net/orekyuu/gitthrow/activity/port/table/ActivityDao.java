package net.orekyuu.gitthrow.activity.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@Dao
@ConfigAutowireable
public interface ActivityDao {

    @Insert
    Result<ActivityTable> insert(ActivityTable activity);

    @Delete
    Result<ActivityTable> delete(ActivityTable activity);

    @Delete(sqlFile = true)
    int deleteByProjectId(String projectId);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<ActivityTable, ?, RESULT> collector);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByUser(String userId, Collector<ActivityTable, ?, RESULT> collector);

    @Select
    Optional<ActivityTable> findById(int id);
}
