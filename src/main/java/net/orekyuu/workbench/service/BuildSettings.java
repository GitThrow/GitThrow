package net.orekyuu.workbench.service;

import java.util.ArrayList;
import java.util.List;

public class BuildSettings {

    private List<String> buildCommand = new ArrayList<>();
    private List<String> artifactPath = new ArrayList<>();

    public List<String> getBuildCommand() {
        return buildCommand;
    }

    public void setBuildCommand(List<String> buildCommand) {
        this.buildCommand = buildCommand;
    }

    public List<String> getArtifactPath() {
        return artifactPath;
    }

    public void setArtifactPath(List<String> artifactPath) {
        this.artifactPath = artifactPath;
    }
}
