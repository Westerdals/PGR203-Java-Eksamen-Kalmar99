package no.kristiania.db;

import no.kristiania.db.Project;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {

    private List<Project> project = new ArrayList<>();

    public ProjectDao(DataSource dataSource) {

    }

    public void insert(Project project) {
        this.project.add(project);
    }

    public List<Project> listAll() {
        return project;
    }
}
