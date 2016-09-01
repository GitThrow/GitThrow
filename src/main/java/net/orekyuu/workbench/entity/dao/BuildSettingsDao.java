package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.BuildSettings;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface BuildSettingsDao {

    @Insert
    int insert(BuildSettings buildSettings);

    @Select
    Optional<BuildSettings> findByProject(String projectId);

    @Update
    int update(BuildSettings settings);

    @Delete
    int delete(BuildSettings settings);
}
