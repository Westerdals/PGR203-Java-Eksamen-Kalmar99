package no.kristiania.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpClientResponse extends HttpMessage {


    public HttpClientResponse(InputStream inputStream) throws IOException {

        super(inputStream);

    }

    public int getStatusCode()
    {
        // F.ex: startLine = "HTTP/1.0 200 OK" split on space = 200
        return Integer.parseInt(startLine.split(" ")[1]);
    }

    public String getHeader(String headerName) {

        return headers.get(headerName.toLowerCase());
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("content-length"));
    }

    public String getBody() {
        return body;
    }
}
