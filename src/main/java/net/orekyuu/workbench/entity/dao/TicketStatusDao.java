package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketStatus;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

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
}
