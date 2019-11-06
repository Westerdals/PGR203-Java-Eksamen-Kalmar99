package no.kristiania.httpserver;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private static int port;
    private ServerSocket serverSocket;
    private String fileLocation;

    public HttpServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) throws IOException {

        HttpServer server = new HttpServer(8080);
        System.out.println("Server running at: localhost:" + server.getPort());
        server.setFileLocation("src/main/resources");
        server.start();
    }

    public void start() throws IOException {
        new Thread(this::run).start();
    }

    private void run(){
        while(true)
            try {
                Socket socket = serverSocket.accept();

                HttpServerRequest request = new HttpServerRequest(socket.getInputStream());

                String requestLine = request.getStartLine();

                String requestTarget = requestLine.split(" ")[1];

                int questionPos = requestTarget.indexOf('?');
                String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0,questionPos);
                if(!requestPath.equals("/echo"))
                {
                    //Create the file and a response with the file content
                    File file = new File(fileLocation + requestPath);


                    int fileTypePos = requestTarget.indexOf('.');
                    String fileType = requestTarget.substring(fileTypePos+1);

                    String contentType = "text/" + fileType;

                    socket.getOutputStream().write(("HTTP/1.1 200 OK\r\n" +
                            "Content-length: " + file.length() + "\r\n" +
                            "Content-type: " + contentType + "\r\n" +
                            "Connection: close\r\n" + "\r\n").getBytes());


                    //Send the file
                    new FileInputStream(file).transferTo(socket.getOutputStream());
                }


                //get parameters
                Map<String, String> requestParameters = parseRequestParameters(requestTarget);
                String statusCode = requestParameters.getOrDefault("status","200");
                String location = requestParameters.get("location");
                String body = requestParameters.getOrDefault("body","Hello World!");

                socket.getOutputStream().write(("HTTP/1.0 " + statusCode + " OK\r\n" +
                        "Content-length: " + body.length() + "\r\n" +
                        (location != null ? "Location: " + location + "\r\n" : "") +
                        "\r\n" +
                        body).getBytes());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private Map<String, String> parseRequestParameters(String requestTarget) {
        Map<String,String> requestParameters = new HashMap<>();
        int questionPos = requestTarget.indexOf("?");
        if (questionPos != -1)
        {
            String query = requestTarget.substring(questionPos+1);
            for (String parameter : query.split("&")) {
                int equalsPos = parameter.indexOf("=");
                String parameterValue = parameter.substring(equalsPos+1);
                String parameterName = parameter.substring(0,equalsPos);
                //Puts request parameters in a hash map.
                requestParameters.put(parameterName,parameterValue);
            }

        }
        return requestParameters;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
