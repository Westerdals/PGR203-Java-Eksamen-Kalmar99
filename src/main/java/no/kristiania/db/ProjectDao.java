package no.kristiania.db;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProjectDao extends AbstractDao<Project> {

    public static final Logger logger = LoggerFactory.getLogger(ProjectDao.class);


    public ProjectDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(Project project, PreparedStatement statement) throws SQLException {
        statement.setString(1, project.getName());
        statement.setInt(2,project.getStatus());
    }

    public void removeObject(int id,String name)
    {
        delete(id,name,"DELETE FROM projects WHERE id = (?)","DELETE FROM project_member WHERE projectname = (?)");
    }

    public void editProject(String newName, String oldName, int id)
    {
        edit(id,newName,null,"UPDATE projects SET name = (?) WHERE id = (?)","UPDATE project_member SET projectname = (?) WHERE projectname = (?)",oldName);
    }


    @Override
    public Project readObject(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String projectName = rs.getString("name");
        String status = rs.getString("status");

        /*

        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT name FROM project_status WHERE id = (?)");
        statement.setInt(1,statusID);
        ResultSet rs2 = statement.executeQuery();
        String status = rs2.getString("name"); */

        Project project = new Project(projectName,status,id);
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
