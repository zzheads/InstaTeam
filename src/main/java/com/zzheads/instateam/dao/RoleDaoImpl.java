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
    SessionFactory mSessionFactory;

    @Override
    public Role findById(Long id) {
        Session session = mSessionFactory.openSession();
        session.beginTransaction();
        Role role = session.get(Role.class, id);
        session.close();
        return role;
    }

    @Override
    public Role findByName(String name) {
        return findAll().stream().filter(r->r.getName().equals(name)).findFirst().get();
    }

    @SuppressWarnings("unchecked")
    public List<Role> findAll() {
        Session session = mSessionFactory.openSession();
        session.beginTransaction();
        List<Role> roles = session.createCriteria(Role.class).list();
        session.close();
        return roles;
    }

    @Override
    public Long save(Role role) {
        Session session = mSessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(role);
        session.getTransaction().commit();
        session.close();
        return role.getId();
    }

    @Override
    public void delete(Role role) {
        Session session = mSessionFactory.openSession();
        session.beginTransaction();
        session.delete(role);
        session.getTransaction().commit();
        session.close();
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
