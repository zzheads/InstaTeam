package com.zzheads.instateam.service;//

import com.zzheads.instateam.dao.CollaboratorDao;
import com.zzheads.instateam.dao.CollaboratorDaoImp;
import com.zzheads.instateam.dao.RoleDao;
import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Role;
import com.zzheads.instateam.web.controller.ProjectController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// InstaTeam
// com.zzheads.instateam.service created by zzheads on 26.07.2016.
//
@Service
public class CollaboratorServiceImpl implements CollaboratorService {
    @Autowired
    private RoleDao mRoleDao;
    @Autowired
    private CollaboratorDao mCollaboratorDao;

    @Override
    public List<Collaborator> findAll() {
        return mCollaboratorDao.findAll();
    }

    @Override
    public List<Collaborator> findAllByRole(Role role) {
        List<Collaborator> all = mCollaboratorDao.findAll();
        return all.stream().filter(c->c.getRole().equals(role)).collect(Collectors.toList());
    }

    @Override
    public Collaborator findById(Long id) {
        return mCollaboratorDao.findById(id);
    }

    @Override
    public Collaborator findByName(String name) {
        return mCollaboratorDao.findByName(name);
    }

    @Override
    public Long save(Collaborator collaborator) {
        Role role = mRoleDao.findByName(collaborator.getRole().getName());
        if (role != null) collaborator.setRole(role);
        return mCollaboratorDao.save(collaborator);
    }

    @Override
    public void delete(Collaborator collaborator) {
        mCollaboratorDao.delete(collaborator);
    }

    @Override
    public void deleteRole(Role role) {
        List<Collaborator> allCollaborators = findAll();
        for (Collaborator c : allCollaborators) {
            if (c.getRole().equals(role)) {
                c.setRole(ProjectController.EMPTY_ROLE);
                mCollaboratorDao.save(c);
            }
        }
    }
}
