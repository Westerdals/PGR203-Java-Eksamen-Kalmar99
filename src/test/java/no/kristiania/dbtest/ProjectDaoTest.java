package no.kristiania.dbtest;

import no.kristiania.db.Project;
import no.kristiania.db.ProjectDao;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDaoTest {

    private static JdbcDataSource dataSource;

    @BeforeEach
    void setUp()
    {
       dataSource = MemberDaoTest.getDataSource();
    }

    @Test
    void shouldFindProject() throws SQLException {
        ProjectDao dao = new ProjectDao(dataSource);

        Project project = getSampleProject();
        dao.insert(project,"insert into projects (name,status) values (?,?)");
        assertThat(dao.listAll("select * from projects")).contains(project);
    }

    public static Project getSampleProject()
    {
        String projectTitle = pickOne(new String[]{"Hilse","Hoppe","Bestå"});
        int status = 1;

        Project testProject = new Project(projectTitle,status);
        return testProject;
    }


    private static String pickOne(String[] strings) {
        return strings[new Random().nextInt(strings.length)];
    }
    private Boolean pickBool(Boolean[] bools) {
        return bools[new Random().nextInt(bools.length)];
    }
}
