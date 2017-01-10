package net.orekyuu.gitthrow.job;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TestLogModel {

    private final List<Line> lines;

    @JsonCreator
    public TestLogModel(@JsonProperty("lines") List<Line> lines) {
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
        public Line(@JsonProperty("content") String content, @JsonProperty("type") OutputType type) {
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
