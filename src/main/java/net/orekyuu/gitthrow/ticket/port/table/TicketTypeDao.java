package net.orekyuu.gitthrow.ticket.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketTypeDao {

    @Insert
    Result<TicketTypeTable> insert(TicketTypeTable type);

    @Update
    Result<TicketTypeTable> save(TicketTypeTable type);

    @Delete
    Result<TicketTypeTable> delete(TicketTypeTable type);

    @Select
    Optional<TicketTypeTable> findById(int id);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<TicketTypeTable, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
