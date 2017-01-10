package net.orekyuu.gitthrow.build.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class Artifact {

    private final int id;
    private final String fileName;

    @JsonCreator
    public Artifact(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return id == artifact.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArtifactTable{");
        sb.append("id=").append(id);
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
