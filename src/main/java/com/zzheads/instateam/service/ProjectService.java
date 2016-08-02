package com.zzheads.instateam.service;//

import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Project;
import com.zzheads.instateam.model.Role;

import java.util.List;

// InstaTeam
// com.zzheads.instateam.service created by zzheads on 30.07.2016.
//
public interface ProjectService {
    Project findById(Long id);
    Project findByName(String name);
    List<Project> findAll();
    Long save(Project project);
    void delete(Project project);
    List<Project> findAllWithCollaborator(Collaborator collaborator);
    void deleteCollaborator(Collaborator collaborator);
    void deleteRole(Role role);
    List<Project> findAllSortedByDate();
}
