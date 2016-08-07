package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.UserAvatar;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface UserAvatarDao {

    @Insert
    int insert(UserAvatar userAvatar) throws DuplicateKeyException;

    @Delete
    int delete(UserAvatar userAvatar);

    @BatchDelete
    int[] delete(Iterable<UserAvatar> userAvatar);

    @Update
    int update(UserAvatar avatar);

    @Select
    Optional<UserAvatar> findById(String id);
}
