package com.zzheads.instateam.dao;//

import com.zzheads.instateam.model.Role;

import java.util.List;

// InstaTeam
// com.zzheads.instateam.dao created by zzheads on 31.07.2016.
//
public interface CrudDao {
    Object findById(Class mClass, Long id);
    Object findByName(Class mClass, String name);
    List findAll(Class mClass);
    void save(Object o);
    void delete(Object o);
}
