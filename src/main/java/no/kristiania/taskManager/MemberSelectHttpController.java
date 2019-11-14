package no.kristiania.taskManager;

import no.kristiania.db.MemberDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class MemberSelectHttpController extends MembersHttpController {



    public MemberSelectHttpController(MemberDao memberDao) {
        super(memberDao);
    }

    @Override
    public String getBody() throws SQLException {

        String body = memberDao.listAll("SELECT * FROM Members").stream()
                .map(p -> String.format("<option id='%s'> %s </option>",p.getId(),p.getName()))
                .collect(Collectors.joining(""));
        return body;
    }
}
