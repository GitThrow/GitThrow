package net.orekyuu.workbench.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * プロジェクトが見つからない時にスローされる
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProjectNotFoundException extends Exception {

    public ProjectNotFoundException(String projectId) {
        super(projectId);
    }
}
