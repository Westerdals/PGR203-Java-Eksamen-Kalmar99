package no.kristiania.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberDao extends AbstractDao<ProjectMember>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectMemberDao.class);

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
        return new ProjectMember(member,name);

    }

    public void removeMember(String name,String project)
    {
        try (Connection conn = dataSource.getConnection();) {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM project_member WHERE membername = (?) AND projectname = (?) ");
            statement.setString(1,name);
            statement.setString(2,project);

            int status = statement.executeUpdate();
            if(status <= 0)
            {
                logger.error("No data was found at membername:{} and projectname: {}, no data was deleted!",name,project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("SQL Exception: " + e);
        }
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
                                boolean dontadd = false;
                                for(String member : p.getMemberArray())
                                {
                                    if(member.equals(rs.getString("membername") ))
                                    {
                                        dontadd = true;
                                    }
                                }
                                if(!dontadd) {
                                    p.addMember(rs.getString("membername"));
                                }
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
