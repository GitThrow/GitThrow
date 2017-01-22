package net.orekyuu.gitthrow.job.util;

import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class NullSseEmitter extends SseEmitter {

    @Override
    protected void extendResponse(ServerHttpResponse outputMessage) {

    }

    @Override
    public void send(Object object) throws IOException {

    }

    @Override
    public void send(Object object, MediaType mediaType) throws IOException {

    }


}
