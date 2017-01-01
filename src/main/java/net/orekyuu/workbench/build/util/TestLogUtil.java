package net.orekyuu.workbench.build.util;

import net.orekyuu.workbench.build.model.domain.TestLog;
import net.orekyuu.workbench.build.port.table.TestLogTable;

public class TestLogUtil {

    public static TestLog fromTable(TestLogTable table) {
        return new TestLog(table.getId(), table.getProjectId(), table.getLog(), table.getCreatedAt(), table.getStatus(), table.getCommit());
    }
}
