package no.kristiania.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpController {


    void handle(String requestAction, String requestPath, OutputStream outputStream, String body, Map<String, String> requestParameters) throws IOException;
}
