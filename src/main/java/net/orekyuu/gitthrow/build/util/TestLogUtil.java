package net.orekyuu.gitthrow.build.util;

import net.orekyuu.gitthrow.build.model.domain.TestLog;
import net.orekyuu.gitthrow.build.port.table.TestLogTable;

public class TestLogUtil {

    public static TestLog fromTable(TestLogTable table) {
        return new TestLog(table.getId(), table.getProjectId(), table.getLog(), table.getCreatedAt(), table.getStatus(), table.getCommit());
    }
}
