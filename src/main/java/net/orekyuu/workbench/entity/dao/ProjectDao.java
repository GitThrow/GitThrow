package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.Project;
import net.orekyuu.workbench.entity.User;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface ProjectDao {

    @Insert
    int insert(Project project) throws DuplicateKeyException;

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findProjectMember(String projectId, Collector<User, ?, RESULT> collector);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByUser(String userId, Collector<Project, ?, RESULT> collector);

    @Update
    int update(Project project);

    @Delete
    int delete(Project project);

    @Select
    Optional<Project> findById(String projectId);
}
