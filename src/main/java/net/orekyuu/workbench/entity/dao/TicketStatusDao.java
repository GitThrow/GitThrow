package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketStatus;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketStatusDao {

    @Insert
    Result<TicketStatus> insert(TicketStatus type);

    @Update
    Result<TicketStatus> save(TicketStatus type);

    @Delete
    Result<TicketStatus> delete(TicketStatus type);

    @Select
    Optional<TicketStatus> findById(int id);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<TicketStatus, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
