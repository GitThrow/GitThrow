package net.orekyuu.workbench.controller.view.user.project;

import net.orekyuu.workbench.git.domain.RemoteRepository;
import net.orekyuu.workbench.git.domain.RemoteRepositoryFactory;
import net.orekyuu.workbench.infra.ProjectMemberOnly;
import net.orekyuu.workbench.infra.ProjectName;
import net.orekyuu.workbench.service.exceptions.ContentNotFoundException;
import net.orekyuu.workbench.service.exceptions.ProjectNotFoundException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class RepositoryBrowseController {

    @Autowired
    private RemoteRepositoryFactory repositoryFactory;

    @ProjectMemberOnly
    @GetMapping(value = "project/{projectId}/raw/{hash}/**")
    public ResponseEntity<byte[]> test(@ProjectName @PathVariable String projectId, @PathVariable String hash,
                                       HttpServletRequest request)
            throws ProjectNotFoundException, GitAPIException {

        String requestPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String[] pathArray = requestPath.split("/");
        // URL Mappingに対して固定の処理になっている
        String relativePath = Arrays.stream(pathArray).skip(5).collect(Collectors.joining("/"));

        RemoteRepository repository = repositoryFactory.create(projectId);
        byte[] bytes = repository.getRepositoryFile(hash, relativePath)
            .orElseThrow(() -> new ContentNotFoundException(projectId))
            .toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(ContentTypeMapping.findType(relativePath));
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        return responseEntity;
    }
    
    // この辺デフォルトでないのかな。(githubのRaw表示みたいな感じにしたい)
    // HTMLはプレーンテキストで転送
    enum ContentTypeMapping{
        TEXT(MediaType.TEXT_PLAIN),
        JPEG(MediaType.IMAGE_JPEG, "jpg"),
        GIF(MediaType.IMAGE_GIF),
        PNG(MediaType.IMAGE_PNG),
        PDF(MediaType.APPLICATION_PDF);
        
        MediaType type;
        List<String> extensions=new ArrayList<>();
        
        ContentTypeMapping(MediaType type, String... extensions){
            this.type=type;
            this.extensions.add(this.name().toLowerCase());
            this.extensions.addAll(Stream.of(extensions).map(String::toLowerCase).collect(Collectors.toList()));
        }
        
        public MediaType getType(){
            return type;
        }
        public static MediaType findType(String fileName){
            String ext=Paths.get(fileName).toFile().getName().split("\\.")[1];
            Optional<MediaType> mediaType=Arrays.stream(values())
                    .filter(type->type.extensions.stream().anyMatch(ext::equals))
                    .findFirst().map(ContentTypeMapping::getType);
            return mediaType.orElse(MediaType.TEXT_PLAIN);
        }
    }
}
