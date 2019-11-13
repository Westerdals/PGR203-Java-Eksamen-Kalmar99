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

    public void insertMembers(Member member) throws SQLException {
        try (Connection conn = dataSource.getConnection();) {
            PreparedStatement statement = conn.prepareStatement(
                    "insert into members (name,email) values (?,?)");
            insertMember(member, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void insertMember(Member member, PreparedStatement statement) throws SQLException {
        statement.setString(1, member.getName());
        statement.setString(2,member.getEmail());
    }

    public Member readObject(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String email = rs.getString("email");
        Member member = new Member(name,email);
        return member;
    }

    public List<Member> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from members"
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<Member> result = new ArrayList<>();

                    while (rs.next()) {

                        result.add(readObject(rs));
                    }
                    return result;
                }
            }
        }
    }


    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Enter a member name to insert");
        String memberName = new Scanner(System.in).nextLine();

        System.out.println("Enter member email");
        String memberEmail = new Scanner(System.in).nextLine();

        Properties properties = new Properties();
        properties.load(new FileReader("task-manager.properties"));

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        MemberDao memberDao = new MemberDao(dataSource);

        memberDao.insertMembers(new Member(memberName, memberEmail));

        System.out.println(memberDao.listAll());
    }
}
