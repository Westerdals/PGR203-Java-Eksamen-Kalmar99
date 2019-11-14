package no.kristiania.taskManager;

import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import no.kristiania.dbtest.MemberDaoTest;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class MembersHttpControllerTest {


    private static JdbcDataSource dataSource;

    @BeforeEach
    void setUp()
    {
        dataSource = MemberDaoTest.getDataSource();
    }

    @Test
    void shouldReturnAllMembers() throws SQLException {
        MemberDao memberDao = new MemberDao(dataSource);
        Member member = MemberDaoTest.getSampleMember();
        memberDao.insert(member,"insert into members (name,email) values (?,?)");


        MembersHttpController controller = new MembersHttpController(memberDao);
        assertThat(controller.getBody()).contains(member.getName());

    }

    @Test
    void shouldCreateNewMember() throws IOException, SQLException {
        MemberDao memberDao = new MemberDao(dataSource);
        MembersHttpController controller = new MembersHttpController(memberDao);

        String requestBody = "memberName=TestMember&email=test@example.com";
        controller.handle("POST","api/members",new ByteArrayOutputStream(),requestBody,null);

        assertThat(memberDao.listAll("select * from members"))
                .contains(new Member("TestMember","test@example.com"));
    }


}
