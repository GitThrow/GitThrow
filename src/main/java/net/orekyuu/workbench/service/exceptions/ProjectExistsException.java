package net.orekyuu.workbench.service.exceptions;

/**
 * すでにプロジェクトが存在する時にスローされる
 */
public class ProjectExistsException extends RuntimeException {
    public ProjectExistsException(String projectId) {
        super("projectId=" + projectId);
    }
}
