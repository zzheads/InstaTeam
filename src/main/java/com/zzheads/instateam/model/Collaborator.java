package com.zzheads.instateam.model;//

// InstaTeam
// com.zzheads.instateam.model created by zzheads on 26.07.2016.
//

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//    DONE: Create the Collaborator model class, which represents a person who is a candidate for working on any given project. Each collaborator should contain the following:
//    DONE: id: auto-generated numeric identifier to serve as the table’s primary key
//    DONE: name: first and last name of the collaborator. This is a required field for data validation.
//    DONE: role: the single Role object that represents this collaborator’s skill. For proper data association, it’s important to keep in mind that there could be many collaborators associated with any one role. This is a required field for data validation.
//    DONE: Getters and setters for all fields
//    DONE: Default constructor

@Entity
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size (min=3)
    private String name;

    @ManyToOne (cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Role role;

    public Collaborator() {}

    public Collaborator(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public void init() {
        name="Unassigned";
        role = new Role("Undefined");
        role.setId(0L);
        id=0L;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Collaborator)) return false;

        Collaborator that = (Collaborator) o;

        return getName().equals(that.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
