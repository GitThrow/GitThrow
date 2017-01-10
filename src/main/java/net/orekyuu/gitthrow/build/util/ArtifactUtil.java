package net.orekyuu.gitthrow.build.util;

import net.orekyuu.gitthrow.build.model.domain.Artifact;
import net.orekyuu.gitthrow.build.port.table.ArtifactTable;

public class ArtifactUtil {

    public static Artifact fromTable(ArtifactTable table) {
        return new Artifact(table.getId(), table.getFileName());
    }
}
