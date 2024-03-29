package no.kristiania.dbtest;

import no.kristiania.db.*;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberDaoTest {

    private static JdbcDataSource dataSource;


    @BeforeEach
    void setUp()
    {
        dataSource = getDataSource();
    }

    @Test
    void shouldRetrieveStoredMembers() throws SQLException {

        MemberDao dao = new MemberDao(dataSource);

        Member member = getSampleMember();
        dao.insert(member,"insert into members (name,email) values (?,?)");

        assertThat(dao.listAll("select * from members")).contains(member);
    }

    private static String pickOne(String[] strings) {
        return strings[new Random().nextInt(strings.length)];
    }

    public static Member getSampleMember()
    {
        String memberName = pickOne(new String[] {"Kriss", "Kalmar", "Andre", "Tredje"});
        String memberEmail = pickOne(new String[] {"Kriss@tull.com", "Kalmartull.com", "Andretull.com", "Tredjetull.com"});

        Member member = new Member(memberName,memberEmail);
        return member;
    }

    public static JdbcDataSource getDataSource()
    {
        dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        Flyway.configure().dataSource(dataSource).load().migrate();

        return dataSource;
    }
}
