package no.kristiania.db;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class MemberDao {

    private DataSource dataSource;

    public MemberDao(DataSource dataSource) { this.dataSource = dataSource; }

    public void insertMembers(String memberName) throws SQLException {

        try (Connection conn = dataSource.getConnection();) {
            PreparedStatement statement = conn.prepareStatement(
                    "insert into members (name) values (?)");
            statement.setString(1, memberName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public List<String> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from members"
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<String> result = new ArrayList<>();

                    while (rs.next()) {
                        result.add(rs.getString("name"));
                    }
                    return result;
                }
            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Enter a member to insert");
        String memberName = new Scanner(System.in).nextLine();

        Properties properties = new Properties();
        properties.load(new FileReader("task-manager.properties"));

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/member");
        dataSource.setUser("member");
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        MemberDao memberDao = new MemberDao(dataSource);
        memberDao.insertMembers(memberName);

        System.out.println(memberDao.listAll());
    }
}
