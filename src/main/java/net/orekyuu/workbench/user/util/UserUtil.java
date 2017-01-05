package net.orekyuu.workbench.user.util;

import net.orekyuu.workbench.user.domain.model.User;
import net.orekyuu.workbench.user.domain.model.UserSettings;
import net.orekyuu.workbench.user.port.table.UserSettingTable;
import net.orekyuu.workbench.user.port.table.UserTable;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UserUtil {

    public static User fromTable(UserTable table) {
        return new User(table.getId(), table.getName(), table.getEmail(), table.isAdmin(), null);
    }

    public static User fromTable(UserTable userTable, UserSettingTable settingTable) {
        UserSettings settings = new UserSettings(settingTable);
        return new User(userTable.getId(), userTable.getName(), userTable.getEmail(), userTable.isAdmin(), settings);
    }

    public static byte[] loadDefaultUserIcon() {
        try(InputStream in = UserUtil.class.getClassLoader().getResourceAsStream("default-user-icon.png")) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            FileCopyUtils.copy(in, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
