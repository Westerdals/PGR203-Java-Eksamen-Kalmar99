package no.kristiania.db;

import java.util.Objects;

public class Project {

    private String name;
    private Boolean status;

    public Project(String name, Boolean status)
    {
        this.name = name;
        this.status = status;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus (Boolean status) {
        this.status = status;
    }

    public String getName()
    {
        return this.name;
    }

    public Boolean getEmail() {
        return this.status;
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
