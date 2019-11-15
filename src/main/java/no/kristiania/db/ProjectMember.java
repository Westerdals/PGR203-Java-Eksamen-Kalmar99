package no.kristiania.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectMember {

    private String memberName;
    private String projectName;

    public ProjectMember(String memberName, String projectName) {
        this.memberName = memberName;
        this.projectName = projectName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMember that = (ProjectMember) o;
        return memberName.equals(that.memberName) &&
                projectName.equals(that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberName, projectName);
    }





}
