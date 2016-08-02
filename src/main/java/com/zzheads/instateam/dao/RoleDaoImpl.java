package com.zzheads.instateam.dao;//

import com.zzheads.instateam.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleDaoImpl extends CrudDaoImpl implements RoleDao {

    @Override
    public Role findById(Long id) {
        return (Role) super.findById(Role.class, id);
    }

    @Override
    public Role findByName(String name) {
        for (Role r : findAll()) {
            if (r.getName().equals(name)) return r;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Role> findAll() {
        return super.findAll(Role.class);
    }

    @Override
    public Long save(Role role) {
        super.save(role);
        return role.getId();
    }

    @Override
    public void delete(Role role) {
        super.delete(role);
    }

    @Override
    public void setRolesId(List<Role> roles) {
        Role role;
        if (roles!=null) {
            for (Role r : roles) {
                role = findByName(r.getName());
                if (role != null) r.setId(role.getId());
            }
        }
    }
}
