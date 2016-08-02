package com.zzheads.instateam.service;//

import com.zzheads.instateam.dao.*;
import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Project;
import com.zzheads.instateam.model.Role;
import com.zzheads.instateam.web.controller.ProjectController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

// InstaTeam
// com.zzheads.instateam.service created by zzheads on 26.07.2016.
//
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private RoleDao mRoleDao;
    @Autowired
    private CollaboratorDao mCollaboratorDao;
    @Autowired
    private ProjectDao mProjectDao;


    @Override
    public List<Project> findAll() {
        return mProjectDao.findAll();
    }

    @Override
    public Project findById(Long id) {
        return mProjectDao.findById(id);
    }

    @Override
    public Project findByName(String name) {
        return mProjectDao.findByName(name);
    }

    @Override
    public Long save(Project project) {
        Role role;
        Collaborator collaborator;

        // Saving new roles (if exists)
        if (project.getRolesNeeded()!=null) {
            for (Role r : project.getRolesNeeded()) {
                role = mRoleDao.findById(r.getId());
                if (role == null) mRoleDao.save(r);
            }
        }

        // Saving new collaborators (if exists)
        if (project.getCollaborators()!=null) {
            for (Collaborator c : project.getCollaborators()) {
                collaborator = mCollaboratorDao.findById(c.getId());
                if (collaborator == null) mCollaboratorDao.save(c);
            }
        }

        return mProjectDao.save(project);
    }

    @Override
    public void delete(Project project) {
        mProjectDao.delete(project);
    }

    public void deleteRole(Role role) {
        List<Project> allProjects = mProjectDao.findAll();
        for (Project p : allProjects) {
            p.deleteRole(role);
            p.fixCollaboratorsAndRoles();
            mProjectDao.save(p);
        }
        mCollaboratorDao.deleteRole(role);
        mRoleDao.delete(role);
    }

    @Override public List<Project> findAllSortedByDate() {
        List<Project> projects = findAll();
        projects.sort(new Comparator<Project>() {
            @Override public int compare(Project o1, Project o2) {
                if (o1.getStartDate() == null) return 1;
                if (o2.getStartDate() == null) return -1;
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        return projects;
    }

    @Override
    public List<Project> findAllWithCollaborator(Collaborator collaborator) {
        return mProjectDao.findAllWithCollaborator(collaborator);
    }

    @Override
    public void deleteCollaborator(Collaborator collaborator) {
        mProjectDao.deleteCollaborator(collaborator);
    }

}
