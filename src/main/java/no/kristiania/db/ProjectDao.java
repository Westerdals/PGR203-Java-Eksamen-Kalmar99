package no.kristiania.db;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ProjectDao extends AbstractDao<Project> {

    public ProjectDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(Project project, PreparedStatement statement) throws SQLException {
        statement.setString(1, project.getName());
        statement.setString(2,project.getStatus());
    }

    @Override
    public Project readObject(ResultSet rs) throws SQLException {
        String projectName = rs.getString("name");
        String status = rs.getString("status");
        Project project = new Project(projectName,status);
        return project;
    }

    public static void main(String[] args) throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("task-manager.properties"));

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        ProjectDao projectDao = new ProjectDao(dataSource);

        //projectDao.insert(new Project ("Test", "In-Progress"),"insert into projects (name,status) values (?,?)");

        System.out.println(projectDao.listAll("select * from projects"));
    }
}
