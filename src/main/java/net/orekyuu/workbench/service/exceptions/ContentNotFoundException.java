package net.orekyuu.workbench.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

/**
 * 要求されたコンテンツが見つからなかった時にスローされる例外
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContentNotFoundException extends RuntimeException {

    private final String projectId;

    public ContentNotFoundException(String projectId) {
        Objects.requireNonNull(projectId);
        this.projectId = projectId;
    }



    public String getProjectId() {
        return projectId;
    }
}
