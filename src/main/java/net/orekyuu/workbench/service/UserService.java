package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.User;
import net.orekyuu.workbench.entity.UserAvatar;
import net.orekyuu.workbench.entity.UserSetting;
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

    /**
     * ユーザーの設定を検索
     * @param id id
     * @return ユーザー
     */
    Optional<UserSetting> findSettingById(String id);

    /**
     * ユーザーのアバターを検索します
     * @param userId ユーザー
     * @return 画像データ
     */
    Optional<byte[]> findAvatar(String userId);

    /**
     * ユーザー情報を更新する
     * @param user 新しいユーザー
     */
    void update(User user);

    /**
     * アバターを更新します
     * @param avatar 新しいアバター
     */
    void updateIcon(UserAvatar avatar);

    /**
     * ユーザーの設定を変更する
     * @param setting 新しい設定
     */
    void updateSetting(UserSetting setting);

}
