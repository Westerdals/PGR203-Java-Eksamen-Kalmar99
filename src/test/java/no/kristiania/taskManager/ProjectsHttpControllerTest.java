package no.kristiania.taskManager;

import no.kristiania.db.Project;
import no.kristiania.db.ProjectDao;
import no.kristiania.dbtest.MemberDaoTest;
import no.kristiania.dbtest.ProjectDaoTest;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
