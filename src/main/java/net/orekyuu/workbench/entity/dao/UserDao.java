package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.User;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;
import java.util.stream.Collector;

@ConfigAutowireable
@Dao
public interface UserDao {
    
    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT selectAll(Collector<User, ?, RESULT> collector);

    @Select
    Optional<User> findById(String id);

    @Insert
    int insert(User user) throws DuplicateKeyException;

    @Update
    int update(User user);
}
