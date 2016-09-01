package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TestSettings;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface TestSettingsDao {
    @Insert
    int insert(TestSettings testSettings);

    @Select
    Optional<TestSettings> findByProject(String projectId);

    @Update
    int update(TestSettings settings);

    @Delete
    int delete(TestSettings settings);
}
