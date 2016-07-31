package com.zzheads.instateam.dao;//

import com.zzheads.instateam.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {
    @Autowired
    CrudDao mCrudDao;

    @Override
    public Role findById(Long id) {
        return (Role) mCrudDao.findById(Role.class, id);
    }

    @Override
    public Role findByName(String name) {
        return findAll().stream().filter(r->r.getName().equals(name)).findFirst().get();
    }

    @SuppressWarnings("unchecked")
    public List<Role> findAll() {
        return mCrudDao.findAll(Role.class);
    }

    @Override
    public Long save(Role role) {
        mCrudDao.save(role);
        return role.getId();
    }

    @Override
    public void delete(Role role) {
        mCrudDao.delete(role);
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
