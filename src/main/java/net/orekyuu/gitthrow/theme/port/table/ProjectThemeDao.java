package net.orekyuu.gitthrow.theme.port.table;


import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface ProjectThemeDao {

    @Insert
    Result<ProjectThemeTable> insert(ProjectThemeTable table);

    @Update
    Result<ProjectThemeTable> save(ProjectThemeTable table);

    @Update(sqlFile = true)
    Result<ProjectThemeTable> saveOpacity(ProjectThemeTable table);

    @Select
    Optional<ProjectThemeTable> findByProject(String projectId);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
