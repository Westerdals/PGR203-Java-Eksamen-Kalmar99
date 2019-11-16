package no.kristiania.db;

import java.util.ArrayList;
import java.util.Objects;

public class Project {

    private String name;
    private int status;
    private ArrayList<String> members = new ArrayList<>();
    int id;
    private String statusname;

    public Project(String name, int status)
    {
        this.name = name;
        this.status = status;
    }

    public Project(String name, String status,int id)
    {
        this.id = id;
        this.name = name;
        this.statusname = status;
    }

    public Project(String name, String status)
    {
        this.name = name;
        this.statusname = status;
    }

    public String getStatusname() {
        return statusname;
    }

    public Project (String name, int status, int id)
    {
        this.name = name;
        this.status = status;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addMember(String member)
    {
        members.add(member);
    }

    public String getMembers()
    {
        String returnthis = "";
        for(String member : members)
        {
            returnthis += member + ", ";
        }

        return returnthis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return status == project.status &&
                id == project.id &&
                Objects.equals(name, project.name) &&
                Objects.equals(members, project.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, members, id);
    }

    public ArrayList<String> getMemberArray() {
        return this.members;
    }
}
