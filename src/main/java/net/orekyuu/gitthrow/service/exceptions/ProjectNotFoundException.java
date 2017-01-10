package net.orekyuu.gitthrow.service.exceptions;

import net.orekyuu.gitthrow.controller.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * プロジェクトが見つからない時にスローされる
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProjectNotFoundException extends ResourceNotFoundException {

    private String projectId;
    public ProjectNotFoundException(String projectId) {
        super(projectId);
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }
}
