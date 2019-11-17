package no.kristiania.taskManager;

import no.kristiania.db.MemberDao;
import no.kristiania.db.ProjectDao;
import no.kristiania.db.ProjectMemberDao;
import no.kristiania.db.StatusDao;
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

        MembersHttpController memberController = new MembersHttpController(new MemberDao(dataSource));

        server.addController("/api/members/add",memberController);
        server.addController("/api/members/select",memberController);
        server.addController("/api/members/remove",memberController);
        server.addController("/api/members/edit",memberController);

        //Handle requests for project_member database
        server.addController("/api/members/projectMember",new ProjectMemberHttpController(new ProjectMemberDao(dataSource)));

        ProjectsHttpController projectController = new ProjectsHttpController(new ProjectDao(dataSource));
        //Handling requests for projects
        server.addController("/api/projects/add",projectController);
        server.addController("/api/projects/select",projectController);
        server.addController("/api/projects/remove",projectController);
        server.addController("/api/projects/edit",projectController);



        server.addController("/api/status/add",new StatusHttpController(new StatusDao(dataSource)));
        server.addController("/api/status/remove",new StatusHttpController(new StatusDao(dataSource)));
        server.addController("/api/status/select",new StatusHttpController(new StatusDao(dataSource)));
        server.addController("/api/status/edit",new StatusHttpController(new StatusDao(dataSource)));

    }

    public static void main(String[] args) throws IOException {
        new TaskManagerServer(8080).start();

    }

    private void start() throws IOException {
        server.start();
    }

}
