package no.kristiania.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClient {

    private String hostname;
    private int port;
    private String requestTarget;
    private String body;
    private Map<String,String> headers = new HashMap<>();

    public HttpClient(String hostname,int port, String requestTarget)
    {
        this.hostname = hostname;
        this.port = port;
        this.requestTarget = requestTarget;
        setRequestHeader("Host",hostname);
        setRequestHeader("Connection","close");
    }

    public HttpClientResponse execute() throws IOException {
        return execute("GET");
    }

    public HttpClientResponse execute(final String httpMethod) throws IOException {
        Socket socket = new Socket(hostname,port);

        if(body != null)
        {
            setRequestHeader("Content-length",String.valueOf(body.length()));
        }

        String headerString = headers.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\r\n"));

        //Sending request to server
        socket.getOutputStream().write((httpMethod + " " + requestTarget + " HTTP/1.1\r\n" +
                headerString +
                "\r\n\r\n" + (body != null ? body : "")).getBytes());
        socket.getOutputStream().flush();

        return new HttpClientResponse(socket.getInputStream());
    }

    public void setRequestHeader(String headerName, String headerValue) {
        headers.put(headerName,headerValue);
    }

    public void setBody(String body) {
        this.body = body;
    }
}

