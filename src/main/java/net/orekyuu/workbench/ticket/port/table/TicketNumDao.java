package net.orekyuu.workbench.ticket.port.table;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

@ConfigAutowireable
@Dao
public interface TicketNumDao {

    @Insert
    Result<TicketNumTable> insert(TicketNumTable num);

    @Update
    Result<TicketNumTable> update(TicketNumTable num);

    @Update(sqlFile = true)
    int increment(String projectId);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
