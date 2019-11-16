package no.kristiania.taskManager;

import no.kristiania.db.Project;
import no.kristiania.db.ProjectDao;
import no.kristiania.db.ProjectMember;
import no.kristiania.db.ProjectMemberDao;
import no.kristiania.dbtest.MemberDaoTest;
import no.kristiania.dbtest.ProjectDaoTest;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectsHttpControllerTest {
    private static JdbcDataSource dataSource;

    @BeforeEach
    void setUp()
    {
        dataSource = MemberDaoTest.getDataSource();
    }

    @Test
    void shouldReturnAllProjects() throws SQLException {
        ProjectDao projectDao = new ProjectDao(dataSource);
        Project project = ProjectDaoTest.getSampleProject();
        projectDao.insert(project,"insert into projects (name,status) values (?,?)");


        ProjectsHttpController controller = new ProjectsHttpController(projectDao);
        assertThat(controller.getBody()).contains(project.getName());
    }

    @Test
    void shouldAddProject() throws SQLException, IOException {
        ProjectDao projectDao = new ProjectDao(dataSource);

        ProjectsHttpController controller = new ProjectsHttpController(projectDao);

        String requestBody = "projectName=Hello&projectStatus=1";

        controller.handle("POST","/api/projects/add",new ByteArrayOutputStream(),requestBody,null);



        Project[] lista = projectDao.listAll("select * from projects").toArray(new Project[0]);
        assertThat(lista[0].getName()).isEqualTo(new Project("Hello",1).getName());

    }

    @Test
    void shouldRemoveProject() throws IOException, SQLException {
        ProjectDao projectDao = new ProjectDao(dataSource);
        ProjectsHttpController controller = new ProjectsHttpController(projectDao);

        String requestBody = "projectName=Hello&projectStatus=1";
        controller.handle("POST","/api/projects/add",new ByteArrayOutputStream(),requestBody,null);

        controller.handle("POST","/api/projects/remove",new ByteArrayOutputStream(),"name=Hello&id=1",null);

        assertThat(projectDao.listAll("SELECT * FROM projects WHERE id = 1")).isEmpty();
    }
}
