package net.orekyuu.workbench.ticket.port.table;


import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;

import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface TicketCommentDao {

    @Insert
    Result<TicketCommentTable> insert(TicketCommentTable comment);

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findByProjectAndTicketNum(String projectId, int ticketNum, Collector<TicketCommentTable, ?, RESULT> collector);

    @Delete(sqlFile = true)
    int deleteByProject(String projectId);

    @Delete
    Result<TicketCommentTable> delete(TicketCommentTable comment);
}
