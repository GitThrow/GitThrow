package net.orekyuu.workbench.service.impl;

import net.orekyuu.workbench.entity.BuildSettings;
import net.orekyuu.workbench.entity.TestSettings;
import net.orekyuu.workbench.entity.dao.BuildSettingsDao;
import net.orekyuu.workbench.entity.dao.TestSettingsDao;
import net.orekyuu.workbench.service.ProjectSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public class ProjectSettingServiceImpl implements ProjectSettingService {

    @Autowired
    private BuildSettingsDao buildSettingsDao;
    @Autowired
    private TestSettingsDao testSettingsDao;

    @Transactional(readOnly = false)
    @Override
    public void setupProjectSetting(String projectId) {
        BuildSettings buildSettings = new BuildSettings();
        buildSettings.projectId = projectId;
        buildSettings.artifactPath = "";
        buildSettings.buildCommand = "";
        buildSettingsDao.insert(buildSettings);

        TestSettings testSettings = new TestSettings();
        testSettings.projectId = projectId;
        testSettings.testCommand = "";
        testSettings.xmlPath = "";
        testSettingsDao.insert(testSettings);
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteProjectSetting(String projectId) {
        BuildSettings buildSettings = new BuildSettings();
        buildSettings.projectId = projectId;
        buildSettingsDao.delete(buildSettings);

        TestSettings testSettings = new TestSettings();
        testSettings.projectId = projectId;
        testSettingsDao.delete(testSettings);
    }

    @Override
    public Optional<BuildSettings> findBuildSettings(String projectId) {
        return buildSettingsDao.findByProject(projectId);
    }

    @Transactional(readOnly = false)
    @Override
    public void updateBuildSettings(BuildSettings buildSettings) {
        buildSettingsDao.update(buildSettings);
    }

    @Override
    public Optional<TestSettings> findTestSettings(String projectId) {
        return testSettingsDao.findByProject(projectId);
    }

    @Transactional(readOnly = false)
    @Override
    public void updateTestSettings(TestSettings testSettings) {
        testSettingsDao.update(testSettings);
    }

}
