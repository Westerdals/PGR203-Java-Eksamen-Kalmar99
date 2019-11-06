package no.kristiania.dbtest;

import no.kristiania.db.MemberDao;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberTest {

    @Test
    void shouldRetrieveStoredMembers() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        dataSource.getConnection().createStatement().executeUpdate(
                "create table members(name varchar(100))"
        );

        MemberDao dao = new MemberDao(dataSource);
        String memberName = pickOne(new String[] {"Kriss", "Kalmar", "Andre", "Tredje"});
        dao.insertMembers(memberName);
        assertThat(dao.listAll()).contains(memberName);
    }

    private String pickOne(String[] strings) {
        return strings[new Random().nextInt(strings.length)];
    }
}
