package net.orekyuu.gitthrow.activity.util;

import net.orekyuu.gitthrow.activity.domain.model.Activity;
import net.orekyuu.gitthrow.activity.port.table.ActivityTable;
import net.orekyuu.gitthrow.user.domain.model.User;

public class ActivityUtil {

    public static Activity fromTable(ActivityTable table, User user) {
        return new Activity(table.getId(), table.getTitle(), table.getDescription(),
            user, table.getProjectId(), table.getCreatedAt());
    }
}
