package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketStatus;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketStatusDao {

    @Insert
    int insert(TicketStatus type);

    @Update
    int save(TicketStatus type);

    @Delete
    int delete(TicketStatus type);

    @Select
    Optional<TicketStatus> findById(int id);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<TicketStatus, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);
}
