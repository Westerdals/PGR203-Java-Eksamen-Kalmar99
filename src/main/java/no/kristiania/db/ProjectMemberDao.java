package no.kristiania.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberDao extends AbstractDao<ProjectMember>{


    public ProjectMemberDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(ProjectMember member, PreparedStatement statement) throws SQLException {
        statement.setString(2, member.getProjectName());
        statement.setString(1,member.getMemberName());
    }

    @Override
    public ProjectMember readObject(ResultSet rs) throws SQLException {
        String name = rs.getString("projectName");
        String member = rs.getString("memberName");
        ProjectMember pmember = new ProjectMember(name,member);

        return pmember;
    }

    public Project readProject(ResultSet rs,List<Project> list) throws SQLException {

        String projectName = rs.getString("name");
        String projectMember = rs.getString("membername");
        String status = rs.getString("status");

        Project returnp = new Project(projectName,status);
        returnp.addMember(projectMember);

        return returnp;
    }

    public List<Project> listAllWithMembers(String sql) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<Project> result = new ArrayList<Project>();

                    while (rs.next()) {

                        Boolean skip = false;
                        for(Project p : result)
                        {
                            if(p.getName().equals(rs.getString("name")))
                            {
                                skip = true;
                                p.addMember(rs.getString("membername"));

                            }
                        }
                        if(!skip) {
                            result.add(readProject(rs, result));
                        }
                    }
                    return result;
                }
            }
        }



    }
}
