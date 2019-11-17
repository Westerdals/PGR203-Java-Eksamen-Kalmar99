package no.kristiania.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

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
            statement2.executeUpdate();

            int status = statement.executeUpdate();
            if(status <= 0)
            {
                logger.error("No data was found at ID:{} and name: {}, no data was deleted!",id,name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("SQL Exception: " + e);
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


    public void edit(int targetID, String parameter1, String parameter2,String sql,String sql2,String parameter3) {
        try (Connection conn = dataSource.getConnection();) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, parameter1);
            if(parameter2 != null)
            {
                statement.setString(2, parameter2);
                statement.setInt(3, targetID);
            } else {
                statement.setInt(2, targetID);
            }



            PreparedStatement statement2 = conn.prepareStatement(sql2);
            statement2.setString(1, parameter1);
            statement2.setString(2, parameter3);

            int status2 = statement2.executeUpdate();

            if (status2 <= 0) {
                logger.error("Could not find any row with Name: {}", parameter1);
            }

            int status = statement.executeUpdate();
            if (status <= 0) {
                logger.error("Could not find any row with id: {}", parameter1);
            }




        } catch (SQLException e) {
            e.printStackTrace();
            MemberDao.logger.error("SQL Exception: {}", e);
        }
    }
}
