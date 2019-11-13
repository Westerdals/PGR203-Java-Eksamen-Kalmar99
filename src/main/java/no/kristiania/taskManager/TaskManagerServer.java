package no.kristiania.taskManager;

import no.kristiania.db.MemberDao;
import no.kristiania.httpserver.HttpServer;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc3.Jdbc3SimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TaskManagerServer {

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
        server.setFileLocation("src/main/resources/task-manager");
        server.addController("/api/members",new MembersHttpController(new MemberDao(dataSource)));
    }

    public static void main(String[] args) throws IOException {
        new TaskManagerServer(8080).start();
    }

    private void start() throws IOException {
        server.start();
    }

}
