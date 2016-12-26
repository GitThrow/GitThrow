package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketNum;
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
    Result<TicketNum> insert(TicketNum num);

    @Update
    Result<TicketNum> update(TicketNum num);

    @Update(sqlFile = true)
    Result<TicketNum> increment(String projectId);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
