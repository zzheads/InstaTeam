package com.zzheads.instateam.web.controller;//

import com.zzheads.instateam.model.Collaborator;
import com.zzheads.instateam.model.Role;
import com.zzheads.instateam.service.*;
import com.zzheads.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleService mRoleService;
    @Autowired
    private CollaboratorService mCollaboratorService;
    @Autowired
    private ProjectService mProjectService;


    // Index of all roles
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public String listRoles(Model model) {
        if(!model.containsAttribute("newRole")) {
            model.addAttribute("newRole",new Role());
        }
        model.addAttribute("page_title", "list roles");
        model.addAttribute("roles", mRoleService.findAll());
        return "roles";
    }

    // Add a role
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public String addRole(@Valid Role newRole, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category",result);
            redirectAttributes.addFlashAttribute("collaborator", newRole);
            // Redirect back to the form
            return "redirect:/roles";
        }

        mRoleService.save(newRole);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role successfully added.", FlashMessage.Status.SUCCESS));
        return "redirect:/roles";
    }

    // Add a role
    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.GET)
    public String editRole(@PathVariable Long roleId, Model model) {
        Role role = mRoleService.findById(roleId);
        List<Collaborator> collaborators = mCollaboratorService.findAllByRole(role);
        model.addAttribute("page_title", "edit role");
        model.addAttribute("role", role);
        model.addAttribute("collaborators", collaborators);
        return "role";
    }

    // Delete a role
    @RequestMapping(value = "/delete/{roleId}", method = RequestMethod.GET)
    public String deleteRole(@PathVariable Long roleId, RedirectAttributes redirectAttributes) {
        Role role = mRoleService.findById(roleId);

        mProjectService.deleteRole(role);
        mCollaboratorService.deleteRole(role);
        mRoleService.delete(role);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Role successfully deleted.", FlashMessage.Status.SUCCESS));
        return "redirect:/roles";
    }

}
