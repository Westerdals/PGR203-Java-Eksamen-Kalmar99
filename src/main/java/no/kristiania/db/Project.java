package no.kristiania.db;

import java.util.Objects;

public class Project {

    private String name;
    private String status;
    int id;

    public Project(String name, String status)
    {
        this.name = name;
        this.status = status;
    }

    public Project (String name, String status, int id)
    {
        this.name = name;
        this.status = status;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return name.equals(project.name) &&
                status.equals(project.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status);
    }
}
