package no.kristiania.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static no.kristiania.httpserver.HttpMessage.readHeaders;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static int port;
    private ServerSocket serverSocket;
    private String fileLocation;



    private HttpController defaultController = new FileHttpController(this);

    private Map<String,HttpController> controllers = new HashMap<>();

    public HttpServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);

        controllers.put("/echo", new EchoHttpController());
    }

    public static void main(String[] args) throws IOException {

        HttpServer server = new HttpServer(8080);
        server.setFileLocation("src/main/resources");
        server.start();
    }

    public void start(){
        new Thread(this::run).start();
        logger.info("Started on: http://localhost: {}",getPort());
    }

    private void run(){
        while(true) {
            try {
                Socket socket = serverSocket.accept();

                //Get request
                HttpServerRequest request = new HttpServerRequest(socket.getInputStream());
                String requestLine = request.getStartLine();
                if (requestLine.isBlank()) continue;

                Map<String, String> headers = request.headers;
                String body = request.body;

                //Get target ex: /index.html
                String requestAction = requestLine.split(" ")[0];
                String requestTarget = requestLine.split(" ")[1];
                int questionPos = requestTarget.indexOf('?');
                String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);
                Map<String, String> requestParameters = parseRequestParameters(requestTarget);

                controllers
                        .getOrDefault(requestPath,defaultController)
                        .handle(requestAction,requestPath,socket.getOutputStream(),body,requestParameters);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static Map<String, String> parseRequestParameters(String requestTarget) {
        int questionPos = requestTarget.indexOf('?');
        if (questionPos > 0)
        {
            String query = requestTarget.substring(questionPos+1);
            return parseQueryString(query);
        }
        return new HashMap<>();
    }

    public static Map<String, String> parseQueryString(String query) {
        Map<String,String> requestParameters = new HashMap<>();
        for (String parameter : query.split("&")) {
            int equalsPos = parameter.indexOf('=');
            String parameterValue = parameter.substring(equalsPos+1);
            String parameterName = parameter.substring(0,equalsPos);
            //Puts request parameters in a hash map.
            requestParameters.put(parameterName,parameterValue);
        }
        return requestParameters;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void addController(String requestPath, HttpController controller) {
        controllers.put(requestPath,controller);
    }
}
