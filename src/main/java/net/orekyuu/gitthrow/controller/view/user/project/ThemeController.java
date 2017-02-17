package net.orekyuu.gitthrow.controller.view.user.project;

import net.orekyuu.gitthrow.project.domain.model.Project;
import net.orekyuu.gitthrow.theme.port.ProjectThemeImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Controller
public class ThemeController {

    @Autowired
    private ProjectThemeImageRepository repository;

    @GetMapping("/project/{projectId}/theme")
    public ResponseEntity<byte[]> showThemeImage(
        Project project,
        @RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSinceHeader) {

        return repository.findByProject(project.getId()).map(it -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setLastModified(it.getLastModified().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            headers.setCacheControl("no-cache");
            if (ifModifiedSinceHeader != null) {
                TemporalAccessor accessor = DateTimeFormatter.RFC_1123_DATE_TIME.parse(ifModifiedSinceHeader);
                LocalDateTime ifModifiedSince = LocalDateTime.from(accessor);
                if (ifModifiedSince.isAfter(it.getLastModified())) {
                    return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_MODIFIED);
                }
            }
            return new ResponseEntity<>(it.getImg(), headers, HttpStatus.OK);
        }).orElse(new ResponseEntity<>((byte[]) null, HttpStatus.NOT_FOUND));

    }
}
