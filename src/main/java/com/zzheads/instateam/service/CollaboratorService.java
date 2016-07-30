package com.zzheads.instateam.service;//

import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Role;

import java.util.List;

// InstaTeam
// com.zzheads.instateam.service created by zzheads on 30.07.2016.
//
public interface CollaboratorService {
    Collaborator findById(Long id);
    Collaborator findByName(String name);
    List<Collaborator> findAll();
    Long save(Collaborator collaborator);
    void delete(Collaborator collaborator);
    List<Collaborator> findAllByRole(Role role);
    void deleteRole(Role role);
}
