package no.kristiania.taskManager;

import no.kristiania.db.ProjectDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class ProjectsSelectHttpControll extends ProjectsHttpController {


    public ProjectsSelectHttpControll(ProjectDao projectDao) {
        super(projectDao);
    }

    @Override
    public String getBody() throws SQLException {
        String body = projectDao.listAll("SELECT * FROM projects").stream()
                .map(p -> String.format("<option id='%s'>%s</option>",p.getId(),p.getName()))
                .collect(Collectors.joining(""));
        return body;
    }
}
