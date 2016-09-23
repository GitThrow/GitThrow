package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TestLog;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Optional;

@ConfigAutowireable
@Dao
public interface TestLogDao {

    @Select
    Optional<TestLog> findById(int id);

    @Insert
    int insert(TestLog testLog);

    @Select
    List<TestLog> findByProject(String projectId);

    @Delete
    int delete(TestLog testLog);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
