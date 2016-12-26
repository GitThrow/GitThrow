package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketType;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketTypeDao {

    @Insert
    Result<TicketType> insert(TicketType type);

    @Update
    Result<TicketType> save(TicketType type);

    @Delete
    Result<TicketType> delete(TicketType type);

    @Select
    Optional<TicketType> findById(int id);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<TicketType, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
