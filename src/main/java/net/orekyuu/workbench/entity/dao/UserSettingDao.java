package net.orekyuu.workbench.entity.dao;

import java.util.Optional;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.springframework.dao.DuplicateKeyException;

import net.orekyuu.workbench.entity.UserAvatar;
import net.orekyuu.workbench.entity.UserSetting;


@ConfigAutowireable
@Dao
public interface UserSettingDao {

    @Insert
    int insert(UserSetting userSetting) throws DuplicateKeyException;

    @Delete
    int delete(UserSetting userSetting);

    @Update
    int update(UserSetting setting);

    @Select
    Optional<UserSetting> findById(String id);

}
