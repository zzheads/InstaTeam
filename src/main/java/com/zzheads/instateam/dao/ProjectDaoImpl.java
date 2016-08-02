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
public class ProjectDaoImpl extends CrudDaoImpl implements ProjectDao {

    @Override
    public Project findById(Long id) {
        return (Project) super.findById(Project.class, id);
    }

    @Override
    public Project findByName(String name) {
        return findAll().stream().filter(p->p.getName().equals(name)).findFirst().get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Project> findAll() {
        return super.findAll(Project.class);
    }

    @Override
    public Long save(Project project) {
        super.save(project);
        return project.getId();
    }

    @Override
    public void delete(Project project) {
        project.setCollaborators(null);
        project.setRolesNeeded(null);
        super.delete(project);
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
            if (p.getCollaborators().contains(collaborator)) {
                List<Collaborator> newCollaborators = new ArrayList<>();
                for (Collaborator c : p.getCollaborators()) {
                    if (c.getId() != collaborator.getId()) {
                        newCollaborators.add(c);
                    } else {
                        newCollaborators.add(ProjectController.EMPTY_COLLABORATOR);
                    }
                }
                p.setCollaborators(newCollaborators);
                save(p);
            }
        }
    }

}
