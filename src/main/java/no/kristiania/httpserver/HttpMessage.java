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


        System.out.println("------------------------ New Request ------------------------");

        System.out.println(startLine);
        String headerLine;


        //Get the header line
        while(!(headerLine = readLine(inputStream)).isBlank())
        {

            int colonPos = headerLine.indexOf(':');
            String headerName = headerLine.substring(0,colonPos).trim();
            String headerValue = headerLine.substring(colonPos+1).trim();
            System.out.println("Header: " + headerName + " -> " + headerValue);
            headers.put(headerName.toLowerCase(),headerValue);
        }
        if (getHeader("content-length") != null)
        {
            this.body = readBytes(inputStream, Integer.parseInt(getHeader("content-length")));
        }
        System.out.println("-------------------------------------------------------------");
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
