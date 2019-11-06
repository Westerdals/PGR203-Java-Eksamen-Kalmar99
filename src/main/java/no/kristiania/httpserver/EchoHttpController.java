package no.kristiania.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class EchoHttpController implements HttpController {
    @Override
    public void handle(String requestPath, OutputStream outputStream, Map<String, String> requestParameters) throws IOException {
        //get parameters
        String statusCode = requestParameters.getOrDefault("status","200");
        String location = requestParameters.get("location");
        String body = requestParameters.getOrDefault("body","Hello World!");

        outputStream.write(("HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-length: " + body.length() + "\r\n" +
                (location != null ? "Location: " + location + "\r\n" : "") +
                "\r\n" +
                body).getBytes());

    }
}
