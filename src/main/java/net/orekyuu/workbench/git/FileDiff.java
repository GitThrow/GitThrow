package net.orekyuu.workbench.git;

import java.util.List;

public class FileDiff {
    private String fileName;
    private List<Line> lines;

    public FileDiff(String fileName, List<Line> lines) {
        this.fileName = fileName;
        this.lines = lines;
    }

    public String getFileName() {
        return fileName;
    }

    public List<Line> getLines() {
        return lines;
    }
}
