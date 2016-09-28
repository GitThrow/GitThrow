package net.orekyuu.workbench.service;

import net.orekyuu.workbench.job.WorkbenchConfig;

import java.util.Optional;

public interface WorkbenchConfigService {

    /**
     * 指定のバージョンのWorkbenchConfigを返します
     * @@param projectId プロジェクトID
     * @param hash バージョン
     * @return WorkbenchConfig
     */
    Optional<WorkbenchConfig> find(String projectId, String hash);
}
