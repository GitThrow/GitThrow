package net.orekyuu.workbench.job;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class TestLogModel {

    private final List<Line> lines;

    @JsonCreator
    public TestLogModel(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public enum OutputType {
        DEFAULT, ERROR
    }
    public static class Line {
        private final String content;
        private final OutputType type;

        @JsonCreator
        public Line(String content, OutputType type) {
            this.content = content;
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public OutputType getType() {
            return type;
        }
    }
}
