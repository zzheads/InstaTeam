package com.zzheads.instateam.web.controller;//

import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Project;
import com.zzheads.instateam.model.Role;
import com.zzheads.instateam.service.*;
import com.zzheads.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    public String addCollaborator(@Valid Collaborator newCollaborator, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category",result);
            redirectAttributes.addFlashAttribute("collaborator", newCollaborator);
            // Redirect back to the form
            return "redirect:/collaborators";
        }

        mCollaboratorService.save(newCollaborator);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Collaborator successfully added.", FlashMessage.Status.SUCCESS));
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
    public String editCollaborator(@Valid Collaborator collaborator, @PathVariable Long collaboratorId, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category",result);
            redirectAttributes.addFlashAttribute("collaborator", collaborator);
            // Redirect back to the form
            return String.format("redirect:/collaborator/%s", collaboratorId);
        }

        // if we changed role of collaborator - we need change that collaborator on empty one
        collaborator.getRole().setName(mRoleService.findById(collaborator.getRole().getId()).getName());
        for (Project p : mProjectService.findAllWithCollaborator(collaborator)) {
            if (!p.getRolesNeeded().contains(collaborator.getRole())) {
                p.getCollaborators().remove(collaborator);
                p.getCollaborators().add(ProjectController.EMPTY_COLLABORATOR);
            }
        }
        mCollaboratorService.save(collaborator);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Collaborator successfully updated.", FlashMessage.Status.SUCCESS));
        return "redirect:/collaborators";
    }

    // Delete a role
    @RequestMapping(value = "collaborator/delete/{collaboratorId}", method = RequestMethod.GET)
    public String deleteCollaborator(@PathVariable Long collaboratorId, RedirectAttributes redirectAttributes) {
        Collaborator collaborator = mCollaboratorService.findById(collaboratorId);

        mProjectService.deleteCollaborator (collaborator);
        mCollaboratorService.delete(collaborator);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Collaborator successfully deleted.", FlashMessage.Status.SUCCESS));
        return "redirect:/collaborators";
    }
}
