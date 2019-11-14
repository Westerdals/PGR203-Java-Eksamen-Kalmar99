package no.kristiania.httpserver;

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;


public class HttpMessage {
    protected String body;
    protected String startLine;
    protected Map<String, String> headers = new HashMap();

    public HttpMessage(InputStream inputStream) throws IOException {
        startLine = readLine(inputStream);

        //Get the header line
        headers = readHeaders(inputStream);

        body = readBody(headers,inputStream);

        if (getHeader("Content-length") != null)
        {
            body = readBytes(inputStream, Integer.parseInt(getHeader("Content-length")));
        }

    }

    static String readBody(Map<String, String> headers, InputStream inputStream) throws IOException {
        if(headers.containsKey("Content-length")) {
            StringBuilder body = new StringBuilder();
            for(int i = 0; i < Integer.parseInt(headers.get("Content-length")); i++)
            {
                body.append((char)inputStream.read());
            }
            return body.toString();
        } else
        {
            return null;
        }
    }

    static Map<String, String> readHeaders(InputStream inputStream) throws IOException {
        Map<String,String> headers = new HashMap<>();
        String headerLine;
        while(!(headerLine = readLine(inputStream)).isBlank())
        {
            int colonPos = headerLine.indexOf(':');
            headers.put(headerLine.substring(0,colonPos).trim().toLowerCase(),
                    headerLine.substring(colonPos+1).trim());
        }
        return headers;
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
        return Integer.parseInt(getHeader("Content-length"));
    }

    public static String readLine(InputStream inputStream) throws IOException {
        StringBuilder line = new StringBuilder();
        int c;
        while((c = inputStream.read()) != -1)
        {
            if (c == '\r')
            {
                inputStream.read();
                break;
            }
            line.append((char)c);
        }

        return line.toString();
    }


    protected String readBytes(InputStream inputStream, int contentLength) throws IOException {
        StringBuilder body = new StringBuilder();
        for(int i = 0; i < contentLength; i++)
        {
            body.append((char)inputStream.read());
        }
        return body.toString();
    }

    public String getStartLine() {
        return startLine;
    }
}
