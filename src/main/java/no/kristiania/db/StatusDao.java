package no.kristiania.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusDao extends AbstractDao<Status> {

    private static final Logger logger = LoggerFactory.getLogger(StatusDao.class);

    public StatusDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(Status status, PreparedStatement statement) throws SQLException {
        statement.setString(1,status.getName());
    }

    public void edit(int id, int id2) {
        try (Connection conn = dataSource.getConnection();) {

            PreparedStatement statement = conn.prepareStatement("UPDATE projects SET status = (?) WHERE id = (?)");
            statement.setInt(1,id2);
            statement.setInt(2,id);

            int status = statement.executeUpdate();
            if(status <= 0)
            {
                logger.error("NO DATA CHANGED");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            MemberDao.logger.error("SQL Exception: " + e);
        }
    }


    public void removeObject(int id,String name)
    {
            delete(id,name, "UPDATE projects SET status = 1 WHERE status = (?)","DELETE FROM project_status WHERE name = (?)");
    }

    @Override
    public Status readObject(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Status status = new Status(id,name);
        return status;
    }
}
