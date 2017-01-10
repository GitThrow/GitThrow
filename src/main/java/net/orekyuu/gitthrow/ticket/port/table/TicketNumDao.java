package net.orekyuu.gitthrow.ticket.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface TicketNumDao {

    @Insert
    Result<TicketNumTable> insert(TicketNumTable num);

    @Update
    Result<TicketNumTable> update(TicketNumTable num);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Select
    Optional<TicketNumTable> findByProjectID(String projectId);
}
