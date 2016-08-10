package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketNum;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

@ConfigAutowireable
@Dao
public interface TicketNumDao {

    @Insert
    int insert(TicketNum num);

    @Update
    int update(TicketNum num);

    @Update(sqlFile = true)
    int increment(String projectId);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
