package no.kristiania.taskManager;

import no.kristiania.db.MemberDao;
import no.kristiania.db.ProjectDao;
import no.kristiania.db.ProjectMemberDao;
import no.kristiania.httpserver.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;






public class TaskManagerServer {

    private static final Logger logger = LoggerFactory.getLogger(TaskManagerServer.class);
    private HttpServer server;

    TaskManagerServer(int port) throws IOException {


        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        Properties properties = new Properties();
        try(FileReader reader = new FileReader("task-manager.properties"))
        {
            properties.load(reader);
        }


        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        server = new HttpServer(port);

        logger.info("Started on: http//localhost:{}", server.getPort());

        server.setFileLocation("src/main/resources/task-manager");

        //Handling requests for members
        server.addController("/api/members/add",new MembersHttpController(new MemberDao(dataSource)));
        server.addController("/api/members/select",new MembersHttpController(new MemberDao(dataSource)));
        server.addController("/api/members/remove",new MembersHttpController(new MemberDao(dataSource)));

        server.addController("/api/members/projectMember",new ProjectMemberHttpController(new ProjectMemberDao(dataSource)));

        //Handling requests for projects
        server.addController("/api/projects/add",new ProjectsHttpController(new ProjectDao(dataSource)));
        server.addController("/api/projects/select",new ProjectsHttpController(new ProjectDao(dataSource)));





    }

    public static void main(String[] args) throws IOException {
        new TaskManagerServer(8080).start();

    }

    private void start() throws IOException {
        server.start();
    }

}
