package net.orekyuu.gitthrow.user.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@ConfigAutowireable
@Dao
public interface UserDao {

    @Select(strategy = SelectType.STREAM)
    <RESULT> RESULT selectAll(Function<Stream<UserTable>, RESULT> stream);

    @Select
    Optional<UserTable> findById(String id);

    @Insert
    Result<UserTable> insert(UserTable user) throws DuplicateKeyException;

    @Update(sqlFile = true)
    Result<UserTable> update(UserTable user);

}
