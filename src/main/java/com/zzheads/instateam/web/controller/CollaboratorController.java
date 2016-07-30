package com.zzheads.instateam.web.controller;//

import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Project;
import com.zzheads.instateam.model.Role;
import com.zzheads.instateam.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class CollaboratorController {
    @Autowired
    private RoleService mRoleService;
    @Autowired
    private CollaboratorService mCollaboratorService;
    @Autowired
    private ProjectService mProjectService;


    // Index of all collaborators
    @RequestMapping(value = "/collaborators", method = RequestMethod.GET)
    public String listCollaborators(Model model) {
        if(!model.containsAttribute("newCollaborator")) {
            model.addAttribute("newCollaborator",new Collaborator());
        }
        model.addAttribute("page_title", "collaborators");
        model.addAttribute("roles", mRoleService.findAll());
        model.addAttribute("collaborators", mCollaboratorService.findAll().toArray());
        return "collaborators";
    }

    // Add a collaborator
    @RequestMapping(value = "/collaborators", method = RequestMethod.POST)
    public String addCollaborator(@ModelAttribute Collaborator newCollaborator) {
        // TODO: Check validation entered data
        if ((newCollaborator.getName().length()>3)&&(newCollaborator.getName().length()<99)) mCollaboratorService.save(newCollaborator);
        return "redirect:/collaborators";
    }

    // Edit collaborator
    @RequestMapping(value = "/collaborator/{collaboratorId}", method = RequestMethod.GET)
    public String editCollaborator(@PathVariable Long collaboratorId, Model model) {
        Collaborator collaborator = mCollaboratorService.findById(collaboratorId);
        List<Role> roles = mRoleService.findAll();
        List<Project> projects = mProjectService.findAllWithCollaborator(collaborator);
        model.addAttribute("page_title", "collaborator details");
        model.addAttribute("projects", projects);
        model.addAttribute("roles", roles);
        model.addAttribute("collaborator", collaborator);
        return "collaborator";
    }

    // Update a collaborator
    @RequestMapping(value = "/collaborator/{collaboratorId}", method = RequestMethod.POST)
    public String editCollaborator(@ModelAttribute Collaborator collaborator, @PathVariable Long collaboratorId) {
        collaborator.getRole().setName(mRoleService.findById(collaborator.getRole().getId()).getName());
        mCollaboratorService.save(collaborator);
        return "redirect:/collaborators";
    }

    // Delete a role
    @RequestMapping(value = "collaborator/delete/{collaboratorId}", method = RequestMethod.GET)
    public String deleteRole(@PathVariable Long collaboratorId) {
        Collaborator collaborator = mCollaboratorService.findById(collaboratorId);

        mProjectService.deleteCollaborator (collaborator);

        return "redirect:/roles";
    }
}
