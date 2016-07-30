package com.zzheads.instateam.model;//

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//    DONE: Create the Role model class, which represents the roles each project could contain, and that need to be filled. Each role will have the following pieces of information associated with it:
//    DONE: id: auto-generated numeric identifier to serve as the table’s primary key
//    DONE: name: alphanumeric, reader-friendly name to be displayed. Example role names might be “developer”, “designer”, or “QA engineer”. This is a required field for data validation.
//    DONE: Getters and setters for all fields
//    DONE: Default constructor

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    public Role() {}

    public Role(String name) {this.name = name;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        return getName().equals(role.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}