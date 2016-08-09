package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.TicketPriority;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface TicketPriorityDao {

    @Insert
    int insert(TicketPriority type);

    @Update
    int save(TicketPriority type);

    @Delete
    int delete(TicketPriority type);

    @Select
    Optional<TicketPriority> findById(int id);
}

