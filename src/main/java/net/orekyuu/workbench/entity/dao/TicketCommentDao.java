package net.orekyuu.workbench.entity.dao;


import net.orekyuu.workbench.entity.TicketComment;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketCommentDao {

    @Insert
    Result<TicketComment> insert(TicketComment comment);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProjectAndTicketNum(String projectId, int ticketNum, Collector<TicketComment, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Delete
    Result<TicketComment> delete(TicketComment comment);
}
