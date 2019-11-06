package no.kristiania.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HttpClient {

    private String hostname;
    private int port;
    private String requestTarget;

    public HttpClient(String hostname,int port, String requestTarget)
    {
        this.hostname = hostname;
        this.port = port;
        this.requestTarget = requestTarget;
    }

    public HttpClientResponse execute() throws IOException {
        Socket socket = new Socket(hostname,port);

        //Sending request to server
        socket.getOutputStream().write(("GET " + requestTarget + " HTTP/1.1\r\n" +
                "Host: " + hostname + "\r\n" +
                "\r\n").getBytes());
        socket.getOutputStream().flush();

        return new HttpClientResponse(socket.getInputStream());
    }

}

