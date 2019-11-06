package no.kristiania.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class MembersController implements HttpController {


    @Override
    public void handle(String requestPath, OutputStream outputStream, Map<String, String> requestParameters) throws IOException {
        //Handle Member Request!
    }
}
