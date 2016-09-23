package net.orekyuu.workbench.service;


public class WorkbenchConfig {
    private BuildSettings buildSettings;
    private TestSettings testSettings;

    public BuildSettings getBuildSettings() {
        return buildSettings;
    }

    public void setBuildSettings(BuildSettings buildSettings) {
        this.buildSettings = buildSettings;
    }

    public TestSettings getTestSettings() {
        return testSettings;
    }

    public void setTestSettings(TestSettings testSettings) {
        this.testSettings = testSettings;
    }
}
