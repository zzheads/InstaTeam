package com.zzheads.instateam.model;//

import com.zzheads.instateam.web.controller.ProjectController;

import java.util.ArrayList;
import java.util.List;

// InstaTeam
// com.zzheads.instateam.model created by zzheads on 30.07.2016.
//
public class Assignment {
    private Role role;
    private List<Collaborator> candidates;
    private Collaborator selected;

    public Assignment(Role role, List<Collaborator> allCollaborators, List<Collaborator> collaborators) {
        this.role = role;
        candidates = new ArrayList<>();
        candidates.add(ProjectController.EMPTY_COLLABORATOR);
        for (Collaborator collaborator : allCollaborators) {
            if (collaborator.getRole().getId().equals(role.getId())) candidates.add(collaborator);
        }
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getRole().getId().equals(role.getId())) selected = collaborator;
        }
        if (selected == null) selected = ProjectController.EMPTY_COLLABORATOR;
        candidates.remove(selected);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Collaborator> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Collaborator> candidates) {
        this.candidates = candidates;
    }

    public Collaborator getSelected() {
        return selected;
    }

    public void setSelected(Collaborator selected) {
        this.selected = selected;
    }
}
