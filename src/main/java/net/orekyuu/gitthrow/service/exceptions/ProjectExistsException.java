package net.orekyuu.gitthrow.service.exceptions;

/**
 * すでにプロジェクトが存在する時にスローされる
 */
public class ProjectExistsException extends RuntimeException {
    public ProjectExistsException(String projectId) {
        super("projectId=" + projectId);
    }
}
