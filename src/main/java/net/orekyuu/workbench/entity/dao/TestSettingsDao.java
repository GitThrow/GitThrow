package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TestSettings;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
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
}
