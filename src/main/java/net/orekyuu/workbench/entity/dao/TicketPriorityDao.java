package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketPriority;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketPriorityDao {

    @Insert
    Result<TicketPriority> insert(TicketPriority type);

    @Update
    Result<TicketPriority> save(TicketPriority type);

    @Delete
    Result<TicketPriority> delete(TicketPriority type);

    @Select
    Optional<TicketPriority> findById(int id);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<TicketPriority, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}

