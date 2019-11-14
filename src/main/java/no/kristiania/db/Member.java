package no.kristiania.db;

import java.util.Objects;

public class Member {

    private String name;
    private String email;
    private int id;

    public Member(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    public Member(String name, String email,int id)
    {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId()
    {
        return id;
    }


    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return name.equals(member.name) &&
                email.equals(member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
