package net.orekyuu.workbench.service;

import net.orekyuu.workbench.entity.BuildSettings;
import net.orekyuu.workbench.entity.TestSettings;

import java.util.Optional;

public interface ProjectSettingService {

    /**
     * プロジェクトの設定を初期化します
     * @param projectId プロジェクトID
     */
    void setupProjectSetting(String projectId);

    /**
     * ビルドの設定を取得
     * @param projectId プロジェクトID
     * @return ビルドの設定
     */
    Optional<BuildSettings> findBuildSettings(String projectId);

    /**
     * ビルドの設定を更新する
     * @param buildSettings ビルドの設定
     */
    void updateBuildSettings(BuildSettings buildSettings);

    /**
     * テストの設定を取得
     * @param projectId プロジェクトID
     * @return テストの設定
     */
    Optional<TestSettings> findTestSettings(String projectId);

    /**
     * テストの設定を更新する
     * @param testSettings テストの設定
     */
    void updateTestSettings(TestSettings testSettings);

}
