package no.kristiania.db;

import no.kristiania.db.Project;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao extends AbstractDao<Project> {

    public ProjectDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(Project project, PreparedStatement statement) throws SQLException {

    }

    @Override
    public Project readObject(ResultSet rs) throws SQLException {
        return null;
    }
}
