package no.kristiania.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class FileHttpController implements HttpController {

    private HttpServer httpServer;

    public FileHttpController(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    @Override
    public void handle(String requestAction, String requestPath, OutputStream outputStream, String body, Map<String, String> requestParameters) throws IOException {
        File file = new File(httpServer.getFileLocation() + requestPath);
        if(file.isDirectory())
        {
            file = new File(file,"index.html");
        }
        if(file.exists())
        {
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-length: " + file.length() + "\r\n" +
                    "Connection: close\r\n" + "\r\n").getBytes());
            try(FileInputStream fileInputStream = new FileInputStream(file))
            {
                fileInputStream.transferTo(outputStream);
            }
        } else {
            outputStream.write(("HTTP/1.1 404 Not found\r\n" +
                    "Content-type: text/plain\r\n" +
                    "Content-length: 13\r\n" +
                    "Connection: close\r\n" + "\r\n" +
                    "404 NOT FOUND"

            ).getBytes());
        }
    }
}
