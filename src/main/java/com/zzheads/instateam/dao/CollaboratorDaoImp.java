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
public class CollaboratorDaoImp extends CrudDaoImpl implements CollaboratorDao {

    @Override
    public Collaborator findById(Long id) {
        return (Collaborator) super.findById(Collaborator.class, id);
    }

    @Override
    public Collaborator findByName(String name) {
        for (Collaborator c : findAll()) {
            if (c.getName().equals(name)) return c;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Collaborator> findAll() {
        return super.findAll(Collaborator.class);
    }

    @Override
    public Long save (Collaborator collaborator) {
        super.save(collaborator);
        return collaborator.getId();
    }

    @Override
    public void delete (Collaborator collaborator) {
        collaborator.setRole(null);
        super.delete(collaborator);
    }

    @Override
    public void deleteRole(Role role) {
        for (Collaborator c : findAll()) {
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
