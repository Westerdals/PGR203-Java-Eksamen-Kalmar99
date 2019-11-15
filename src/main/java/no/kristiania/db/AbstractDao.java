package no.kristiania.db;

import javax.sql.DataSource;
import javax.swing.text.html.parser.Entity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> {
    protected DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(T member, String sql){
        try (Connection conn = dataSource.getConnection();) {
            PreparedStatement statement = conn.prepareStatement(sql);
            insertObject(member, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    protected void delete(int id, String name, String sql1, String sql2) {
        try (Connection conn = dataSource.getConnection();) {
            PreparedStatement statement = conn.prepareStatement(sql1);
            statement.setInt(1,id);

            PreparedStatement statement2 = conn.prepareStatement(sql2);
            statement2.setString(1,name);

            int status = statement.executeUpdate();
            if(status <= 0)
            {
                MemberDao.logger.error("No data was found at ID:{} and name: {}, no data was deleted!",id,name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            MemberDao.logger.error("SQL Exception: " + e);
        }
    }

    protected abstract void insertObject(T member, PreparedStatement statement) throws SQLException;

    public abstract T readObject(ResultSet rs) throws SQLException;

    public List<T> listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<T> result = new ArrayList<>();

                    while (rs.next()) {

                        result.add(readObject(rs));
                    }
                    return result;
                }
            }
        }
    }
}
