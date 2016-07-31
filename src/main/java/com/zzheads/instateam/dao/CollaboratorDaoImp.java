package com.zzheads.instateam.dao;//

import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Role;
import com.zzheads.instateam.web.controller.ProjectController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CollaboratorDaoImp implements CollaboratorDao {
    @Autowired
    CrudDao mCrudDao;

    @Override
    public Collaborator findById(Long id) {
        return (Collaborator) mCrudDao.findById(Collaborator.class, id);
    }

    @Override
    public Collaborator findByName(String name) {
        return findAll().stream().filter(c->c.getName().equals(name)).findFirst().get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Collaborator> findAll() {
        return mCrudDao.findAll(Collaborator.class);
    }

    @Override
    public Long save (Collaborator collaborator) {
        mCrudDao.save(collaborator);
        return collaborator.getId();
    }

    @Override
    public void delete (Collaborator collaborator) {
        mCrudDao.delete(collaborator);
    }

    @Override
    public void deleteRole(Role role) {
        List<Collaborator> allCollaborators = findAll();
        for (Collaborator c : allCollaborators) {
            if (c.getRole().equals(role)) {
                c.setRole(ProjectController.EMPTY_ROLE);
                save(c);
            }
        }
    }

    @Override
    public List<Collaborator> findAllByRole(Role role) {
        List<Collaborator> all = findAll();
        return all.stream().filter(c->c.getRole().equals(role)).collect(Collectors.toList());
    }
}
