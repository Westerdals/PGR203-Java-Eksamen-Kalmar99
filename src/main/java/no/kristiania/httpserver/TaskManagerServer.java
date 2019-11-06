package no.kristiania.httpserver;

import java.io.IOException;

public class TaskManagerServer {

    private HttpServer server;

    TaskManagerServer(int port) throws IOException {
        server = new HttpServer(port);
        server.setFileLocation("src/main/resources/task-manager");
        server.addController("/api/members",new MembersController());
    }

    public static void main(String[] args) throws IOException {
        new TaskManagerServer(8080).start();
    }

    private void start() throws IOException {
        server.start();
    }

}
