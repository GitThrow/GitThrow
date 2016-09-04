package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.Artifact;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface ArtifactDao {

    @Insert
    Result<Artifact> insert(Artifact artifact);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<Artifact, ?, RESULT> collector);

    @Select
    Optional<Artifact> findById(int id);

    @Delete
    Result<Artifact> delete(Artifact artifact);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
