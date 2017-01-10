package net.orekyuu.gitthrow.git;

public class Line {

    public static final int NULL_LINE_NUMBER = -1;

    private int oldLineNum;
    private int newLineNum;
    private String content;
    private String change;

    public Line(int oldLineNum, int newLineNum, String content, ChangeType changeType) {
        this.oldLineNum = oldLineNum;
        this.newLineNum = newLineNum;
        this.content = content;
        this.change = changeType.getTypeName();
    }

    public int getOldLineNum() {
        return oldLineNum;
    }

    public int getNewLineNum() {
        return newLineNum;
    }

    public String getContent() {
        return content;
    }

    public String getChange() {
        return change;
    }

    @Override
    public String toString() {
        return String.format("%s | %d | %d | %s", change, oldLineNum, newLineNum, content);
    }
}
