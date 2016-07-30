package com.zzheads.instateam.dao;//

import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Role;

import java.util.List;

// InstaTeam
// com.zzheads.instateam.dao created by zzheads on 30.07.2016.
//
public interface CollaboratorDao {
    Collaborator findById(Long id);
    Collaborator findByName(String name);
    List<Collaborator> findAll();
    Long save(Collaborator collaborator);
    void delete(Collaborator collaborator);
    void deleteRole(Role role);
    List<Collaborator> findAllByRole(Role role);
}
