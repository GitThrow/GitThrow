package net.orekyuu.workbench.build.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface ArtifactDao {

    @Insert
    Result<ArtifactTable> insert(ArtifactTable artifactTable);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<ArtifactTable, ?, RESULT> collector);

    @Select
    Optional<ArtifactTable> findById(int id);

    @Delete
    Result<ArtifactTable> delete(ArtifactTable artifactTable);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
