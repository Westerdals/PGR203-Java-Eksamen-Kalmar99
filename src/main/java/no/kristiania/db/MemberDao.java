package no.kristiania.db;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class MemberDao extends AbstractDao<Member> {

    public MemberDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(Member member, PreparedStatement statement) throws SQLException {
        statement.setString(1, member.getName());
        statement.setString(2,member.getEmail());
    }

    @Override
    public Member readObject(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String email = rs.getString("email");
        Member member = new Member(name,email);
        return member;
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

        memberDao.insert(new Member(memberName, memberEmail),"insert into members (name,email) values (?,?)");

        System.out.println(memberDao.listAll("select * from members"));
    }
}
