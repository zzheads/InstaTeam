package com.zzheads.instateam.model;//

// InstaTeam
// com.zzheads.instateam.model created by zzheads on 26.07.2016.
//

//    DONE: Create the Project model class, which represents a project for which a project manager is seeking collaborators. Each project should contain the following:
//    DONE: id: auto-generated numeric identifier to serve as the table’s primary key
//    DONE: name: alphanumeric, reader-friendly name to be displayed. This is a required field for data validation.
//    DONE: description: longer description of the project. This is a required field for data validation.
//    DONE: status: alphanumeric status of the project, for example “recruiting” or “on hold”
//    DONE: rolesNeeded: collection of Role objects representing all roles needed for this project, regardless of whether or not they’ve been filled. For proper data association, keep in mind that there could be many projects that contain many Role objects. That is, each project can have many roles that it needs, and each role can be needed by many projects.
//    DONE: collaborators: collection of Collaborator objects representing any collaborators that have been assigned to this project. For data association, use the fact that there could be many projects that contain many Collaborator objects. That is, each project can have many collaborators, and each collaborator can work on many projects.
//    DONE: Getters and setters for all fields
//    DONE: Default constructor





import com.zzheads.instateam.web.controller.ProjectController;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size (min = 3)
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    private Status status;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Role> rolesNeeded;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Collaborator> collaborators;

    public Project() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Role> getRolesNeeded() {
        return rolesNeeded;
    }

    public void setRolesNeeded(List<Role> rolesNeeded) {
        this.rolesNeeded = rolesNeeded;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void deleteRole (Role role) {
        rolesNeeded = rolesNeeded.stream().filter(r->!r.equals(role)).collect(Collectors.toList());
        collaborators = collaborators.stream().filter(c->!c.getRole().equals(role)).collect(Collectors.toList());
    }

    public void deleteCollaborator (Collaborator collaborator) {
        collaborators = collaborators.stream().filter(c->!c.equals(collaborator)).collect(Collectors.toList());
    }

    public void fixCollaboratorsAndRoles () {
        // We set up for every roleNeeded - collaborator (if there is no with such role, then empty)
        // Other collaborators (with role not needed) - remove
        List<Collaborator> newCollaborators = new ArrayList<>();

        if (collaborators!=null) {
            if (rolesNeeded != null)
                for (Role rN : rolesNeeded) {
                    for (Collaborator c : collaborators) {
                        if (rN.equals(c.getRole())) {
                            newCollaborators.add(c);
                        }
                    }
                }
            collaborators.clear();
        }
        collaborators = newCollaborators;

        int sizeCol = collaborators.size();
        int sizeRol = rolesNeeded.size();
        if (sizeCol<sizeRol) { // less Collaborators than RolesNeeded - fill empty
            for (int i=sizeCol;i<sizeRol;i++) {
                collaborators.add(ProjectController.EMPTY_COLLABORATOR);
            }
        }

    }

    @Override
    public String toString() {
        return name;
    }
}
