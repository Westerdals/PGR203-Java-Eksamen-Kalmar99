package no.kristiania.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MemberDao {

    private List<String> members = new ArrayList<>();

    public void insertMembers(String memberName) {
        members.add(memberName);
    }

    public List<String> listAll() {
        return members;
    }
}
