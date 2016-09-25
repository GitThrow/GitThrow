package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.entity.User;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

@ConfigAutowireable
@Dao
public interface ProjectDao {

    @Insert
    int insert(Project project) throws DuplicateKeyException;

    @Select(strategy = SelectType.STREAM)
    <RESULT> RESULT findProjectMember(String projectId, Function<Stream<User>, RESULT> collector);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByUser(String userId, Collector<Project, ?, RESULT> collector);

    @Update
    int update(Project project);

    @Delete
    int delete(Project project);

    @Select
    Optional<Project> findById(String projectId);
}
