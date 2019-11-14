package no.kristiania.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class EchoHttpController implements HttpController {

    @Override
    public void handle(String requestAction, String path, OutputStream outputStream, String body, Map<String, String> requestParameters) throws IOException {
        //get parameters
        if(requestAction.equals("POST"))
        {
            requestParameters = HttpServer.parseQueryString(body);
        }

        String statusCode = requestParameters.getOrDefault("status","200");
        String location = requestParameters.get("location");
        String contentType = requestParameters.getOrDefault("content-type","text/plain");
        String responseBody = requestParameters.getOrDefault("body","Hello World!");

        outputStream.write(("HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-type:" + contentType + "\r\n" +
                "Content-length: " + responseBody.length() + "\r\n" +
                "Connection: close\r\n" +
                (location != null ? "Location: " + location + "\r\n" : "") +
                "\r\n" +
                responseBody).getBytes());

    }


}
