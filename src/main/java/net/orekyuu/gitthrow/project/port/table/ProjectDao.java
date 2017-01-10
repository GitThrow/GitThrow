package net.orekyuu.gitthrow.project.port.table;

import net.orekyuu.gitthrow.user.port.table.UserTable;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@ConfigAutowireable
@Dao
public interface ProjectDao {

    @Insert
    Result<ProjectTable> insert(ProjectTable project) throws DuplicateKeyException;

    @Select(strategy = SelectType.STREAM)
    <RESULT> RESULT findProjectMember(String projectId, Function<Stream<UserTable>, RESULT> streamFunc);

    @Select(strategy = SelectType.STREAM)
    <RESULT> RESULT findByUser(String userId, Function<Stream<ProjectTable>, RESULT> streamFunc);

    @Update
    Result<ProjectTable> update(ProjectTable project);

    @Delete
    Result<ProjectTable> delete(ProjectTable project);

    @Select
    Optional<ProjectTable> findById(String projectId);
}
