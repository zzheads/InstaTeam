package com.zzheads.instateam.dao;//

import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Project;
import com.zzheads.instateam.model.Role;
import com.zzheads.instateam.web.controller.ProjectController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// InstaTeam
// com.zzheads.instateam.dao created by zzheads on 30.07.2016.
//
@Repository
public class ProjectDaoImpl implements ProjectDao {
    @Autowired
    CrudDao mCrudDao;

    @Override
    public Project findById(Long id) {
        return (Project) mCrudDao.findById(Project.class, id);
    }

    @Override
    public Project findByName(String name) {
        return findAll().stream().filter(p->p.getName().equals(name)).findFirst().get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Project> findAll() {
        return mCrudDao.findAll(Project.class);
    }

    @Override
    public Long save(Project project) {
        mCrudDao.save(project);
        return project.getId();
    }

    @Override
    public void delete(Project project) {
        mCrudDao.delete(project);
    }

    @Override
    public List<Project> findAllWithCollaborator(Collaborator collaborator) {
        List<Project> projects = findAll();
        List<Project> projectWithCollaborator = new ArrayList<>();
        for (Project p : projects) {
            for (Collaborator c : p.getCollaborators()) {
                if (Objects.equals(c.getId(), collaborator.getId())) projectWithCollaborator.add(p);
            }
        }
        return projectWithCollaborator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteCollaborator(Collaborator collaborator) {
        List<Project> projects = findAll();
        for (Project p : projects) {
            for (Collaborator c : p.getCollaborators()) {
                if (Objects.equals(c.getId(), collaborator.getId())) {
                    p.getCollaborators().remove(c);
                    p.getCollaborators().add(ProjectController.EMPTY_COLLABORATOR);
                }
            }
        }
    }

}
