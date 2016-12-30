package net.orekyuu.workbench.ticket.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketPriorityDao {

    @Insert
    Result<TicketPriorityTable> insert(TicketPriorityTable type);

    @Update
    Result<TicketPriorityTable> save(TicketPriorityTable type);

    @Delete
    Result<TicketPriorityTable> delete(TicketPriorityTable type);

    @Select
    Optional<TicketPriorityTable> findById(int id);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<TicketPriorityTable, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}

