package com.zzheads.instateam.dao;//

import com.zzheads.instateam.model.Role;

import java.util.List;

// InstaTeam
// com.zzheads.instateam.dao created by zzheads on 30.07.2016.
//
public interface RoleDao {
    Role findById(Long id);
    Role findByName(String name);
    List<Role> findAll();
    Long save(Role role);
    void delete(Role role);
    void setRolesId(List<Role> roles);
}
