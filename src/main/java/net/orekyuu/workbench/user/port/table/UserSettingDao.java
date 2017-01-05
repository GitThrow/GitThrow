package net.orekyuu.workbench.user.port.table;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;


@ConfigAutowireable
@Dao
public interface UserSettingDao {

    @Insert
    Result<UserSettingTable> insert(UserSettingTable userSettingTable) throws DuplicateKeyException;

    @Delete
    Result<UserSettingTable> delete(UserSettingTable userSettingTable);

    @Update
    Result<UserSettingTable> update(UserSettingTable setting);

    @Select
    Optional<UserSettingTable> findById(String id);

}
