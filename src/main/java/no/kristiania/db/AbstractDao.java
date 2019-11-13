package no.kristiania.db;

import javax.sql.DataSource;
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

    public void insert(T member, String sql) throws SQLException {
        try (Connection conn = dataSource.getConnection();) {
            PreparedStatement statement = conn.prepareStatement(sql);
            insertObject(member, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    protected abstract void insertObject(T member, PreparedStatement statement) throws SQLException;

    public abstract T readObject(ResultSet rs) throws SQLException;

    public List<T> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from members"
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
