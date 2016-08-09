package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketType;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketTypeDao {

    @Insert
    int insert(TicketType type);

    @Update
    int save(TicketType type);

    @Delete
    int delete(TicketType type);

    @Select
    Optional<TicketType> findById(int id);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProject(String projectId, Collector<TicketType, ?, RESULT> collector);
}
