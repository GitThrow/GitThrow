package net.orekyuu.workbench.ticket.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketStatusDao {

    @Insert
    Result<TicketStatusTable> insert(TicketStatusTable type);

    @Update
    Result<TicketStatusTable> save(TicketStatusTable type);

    @Delete
    Result<TicketStatusTable> delete(TicketStatusTable type);

    @Select
    Optional<TicketStatusTable> findById(int id);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<TicketStatusTable, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
