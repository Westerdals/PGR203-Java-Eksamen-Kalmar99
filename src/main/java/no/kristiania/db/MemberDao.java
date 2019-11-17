package no.kristiania.db;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class MemberDao extends AbstractDao<Member> {

    public static final Logger logger = LoggerFactory.getLogger(MemberDao.class);
    public MemberDao(DataSource dataSource) {
        super(dataSource);
    }


    public void removeObject(int id,String name)
    {

        delete(id, name,"DELETE FROM members WHERE id = (?)","DELETE FROM project_member where membername = (?)");
    }


    @Override
    protected void insertObject(Member member, PreparedStatement statement) throws SQLException {
        statement.setString(1, member.getName());
        statement.setString(2,member.getEmail());
    }

    @Override
    public Member readObject(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        Member member = new Member(name,email,id);
        return member;
    }

    public void editMember(int targetID,String name,String email,String oldName)
    {
        edit(targetID,name,email,"UPDATE members SET name = (?), email = (?) WHERE id = (?)","UPDATE project_member SET membername = (?) WHERE membername = (?)",oldName);
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
        Flyway.configure().dataSource(dataSource).load().migrate();
        MemberDao memberDao = new MemberDao(dataSource);

        memberDao.insert(new Member(memberName, memberEmail),"insert into members (name,email) values (?,?)");

        System.out.println(memberDao.listAll("select * from members"));
    }
}
