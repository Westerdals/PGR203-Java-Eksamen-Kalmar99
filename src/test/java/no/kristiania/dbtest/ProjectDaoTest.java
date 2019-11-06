package no.kristiania.dbtest;

import no.kristiania.db.Project;
import no.kristiania.db.ProjectDao;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDaoTest {

    private DataSource dataSource;

    @Test
    void shouldFindSavedMembers() {
        Project project = new  Project();
        ProjectDao dao = new ProjectDao(dataSource);

        dao.insert(project);
        assertThat(dao.listAll()).contains(project);
    }
}
