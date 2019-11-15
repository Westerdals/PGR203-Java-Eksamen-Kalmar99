package no.kristiania.db;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectMemberDao extends AbstractDao<ProjectMember>{


    public ProjectMemberDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(ProjectMember member, PreparedStatement statement) throws SQLException {
        statement.setString(1, member.getProjectName());
        statement.setString(2,member.getMemberName());
    }

    @Override
    public ProjectMember readObject(ResultSet rs) throws SQLException {
        String name = rs.getString("projectName");
        String member = rs.getString("memberName");
        ProjectMember pmember = new ProjectMember(name,member);
        return pmember;
    }
}
