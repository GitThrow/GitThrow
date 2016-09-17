package net.orekyuu.workbench.git;

public enum ChangeType {

    ADD("add"), DELETE("delete"), EQUAL("equal");

    private final String typeName;

    ChangeType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
