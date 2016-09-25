package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.User;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@ConfigAutowireable
@Dao
public interface UserDao {

    @Select(strategy = SelectType.STREAM)
    <RESULT> RESULT selectAll(Function<Stream<User>, RESULT> collector);

    @Select
    Optional<User> findById(String id);

    @Insert
    int insert(User user) throws DuplicateKeyException;

    @Update
    int update(User user);

}
