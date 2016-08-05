package net.orekyuu.workbench.entity.dao;

import net.orekyuu.workbench.entity.User;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Optional;

@ConfigAutowireable
@Dao
public interface UserDao {
    
    @Select
    List<User> selectAll();

    @Select
    Optional<User> findById(String id);
}
