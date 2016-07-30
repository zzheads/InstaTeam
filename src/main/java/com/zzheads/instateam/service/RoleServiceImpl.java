package com.zzheads.instateam.service;//

import com.zzheads.instateam.dao.RoleDao;
import com.zzheads.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// InstaTeam
// com.zzheads.instateam.service created by zzheads on 26.07.2016.
//
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao mRoleDao;

    @Override
    public List<Role> findAll() {
        return mRoleDao.findAll();
    }

    @Override
    public Role findById(Long id) {
        return mRoleDao.findById(id);
    }

    @Override
    public Role findByName(String name) {
        return mRoleDao.findByName(name);
    }

    @Override
    public Long save(Role role) {
        return mRoleDao.save(role);
    }

    @Override
    public void delete(Role role) {
        mRoleDao.delete(role);
    }

    @Override
    public void setRolesId(List<Role> roles) {
        mRoleDao.setRolesId(roles);
    }
}
