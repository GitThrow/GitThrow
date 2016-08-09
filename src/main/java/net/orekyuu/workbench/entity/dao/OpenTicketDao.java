package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.OpenTicket;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

@ConfigAutowireable
@Dao
public interface OpenTicketDao {

    @Insert
    int insert(OpenTicket ticket);

    @Update
    int update(OpenTicket ticket);
}
