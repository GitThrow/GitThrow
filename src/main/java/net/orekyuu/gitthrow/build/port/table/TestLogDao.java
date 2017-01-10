package net.orekyuu.gitthrow.build.port.table;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.List;
import java.util.Optional;

@ConfigAutowireable
@Dao
public interface TestLogDao {

    @Select
    Optional<TestLogTable> findById(int id);

    @Insert
    Result<TestLogTable> insert(TestLogTable testLog);

    @Select
    List<TestLogTable> findByProject(String projectId);

    @Delete
    Result<TestLogTable> delete(TestLogTable testLog);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
