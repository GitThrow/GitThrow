package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.service.exceptions.UserExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    /**
     * ユーザーを作成する
     * @param id ユーザーID
     * @param name 表示名
     * @param rawPassword 生のパスワード
     * @param admin 管理者かどうか
     */
    void createUser(String id, String name, String rawPassword, boolean admin) throws UserExistsException;

    /**
     * 通常のユーザーを作成します
     * @param id ユーザーID
     * @param name 表示名
     * @param rawPassword 生のパスワード
     */
    default void createUser(String id, String name, String rawPassword) throws UserExistsException {
        createUser(id, name, rawPassword, false);
    }

    /**
     * ユーザーを検索
     * @param id id
     * @return ユーザー
     */
    Optional<User> findById(String id);
}
