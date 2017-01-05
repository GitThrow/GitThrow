package net.orekyuu.workbench.ticket.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface OpenTicketDao {

    @Insert(sqlFile = true)
    Result<OpenTicketTable> insert(OpenTicketTable ticket);

    @Update(sqlFile = true)
    Result<OpenTicketTable> update(OpenTicketTable ticket);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<OpenTicketTable, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Select
    Optional<OpenTicketTable> findByProjectAndNum(String projectId, int ticketNum);
}
