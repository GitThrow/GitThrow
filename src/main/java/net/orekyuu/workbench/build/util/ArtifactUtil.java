package net.orekyuu.workbench.build.util;

import net.orekyuu.workbench.build.model.domain.Artifact;
import net.orekyuu.workbench.build.port.table.ArtifactTable;

public class ArtifactUtil {

    public static Artifact fromTable(ArtifactTable table) {
        return new Artifact(table.getId(), table.getFileName());
    }
}
