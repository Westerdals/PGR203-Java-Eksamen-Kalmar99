package no.kristiania.dbtest;

import no.kristiania.db.MemberDao;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberTest {

    @Test
    void shouldRetrieveStoredMembers() {
        MemberDao dao = new MemberDao();
        String memberName = pickOne(new String[] {"Kriss", "Kalmar", "Andre", "Tredje"});
        dao.insertMembers(memberName);
        assertThat(dao.listAll()).contains(memberName);
    }

    private String pickOne(String[] strings) {
        return strings[new Random().nextInt(strings.length)];
    }
}
