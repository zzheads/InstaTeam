package com.zzheads.instateam.web.controller;//

import com.zzheads.instateam.model.*;
import com.zzheads.instateam.service.*;
import com.zzheads.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// InstaTeam
// com.zzheads.instateam.web.controller created by zzheads on 27.07.2016.
//
@Controller
public class ProjectController {
    @Autowired
    private ProjectService mProjectService;
    @Autowired
    private RoleService mRoleService;
    @Autowired
    private CollaboratorService mCollaboratorService;

    private static final String EMPTY_ROLE_TEXT = "Undefined";
    private static final String EMPTY_COLLABORATOR_TEXT = "Unassigned";
    public static Role EMPTY_ROLE;
    public static Collaborator EMPTY_COLLABORATOR;


    // Index
    @RequestMapping(value = {"/", "/projects"})
    public String index(Model model) {
        EMPTY_ROLE = mRoleService.findByName(EMPTY_ROLE_TEXT);
        EMPTY_COLLABORATOR = mCollaboratorService.findByName(EMPTY_COLLABORATOR_TEXT);
        if (EMPTY_ROLE == null) {
            EMPTY_ROLE = new Role(EMPTY_ROLE_TEXT);
            mRoleService.save(EMPTY_ROLE);
            EMPTY_ROLE.setId(mRoleService.findByName(EMPTY_ROLE_TEXT).getId());
        }
        if (EMPTY_COLLABORATOR == null) {
            EMPTY_COLLABORATOR = new Collaborator(EMPTY_COLLABORATOR_TEXT, EMPTY_ROLE);
            mCollaboratorService.save(EMPTY_COLLABORATOR);
            EMPTY_COLLABORATOR.setId(mCollaboratorService.findByName(EMPTY_COLLABORATOR_TEXT).getId());
        }

        model.addAttribute("page_title", "index");
        model.addAttribute("projects", mProjectService.findAllSortedByDate());
        model.addAttribute("statuses", Status.values());
        return "projects";
    }

    @RequestMapping("/project_detail/{projectId}")
    public String projectDetails(@PathVariable Long projectId, Model model) {
        Project project = mProjectService.findById(projectId);
        model.addAttribute("page_title", "project details");
        model.addAttribute("project", project);
        return "project_detail";
    }

    @RequestMapping("/project_new")
    public String createProject(@ModelAttribute Project project, Model model) {
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", new Project());
        }
        List<Role> roles = mRoleService.findAll();

        project.setStartDate(new Date());

        model.addAttribute("page_title", "create project");
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", Status.values());

        return "project_new";
    }

    @RequestMapping(value = "/project_new", method = RequestMethod.POST)
    public String createProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category",result);
            redirectAttributes.addFlashAttribute("project", project);
            // Redirect back to the form
            return "redirect:/project_new";
        }

        // Fill id field for project.rolesNeeded
        mRoleService.setRolesId(project.getRolesNeeded());
        project.fixCollaboratorsAndRoles();

        mProjectService.save(project);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Project successfully created.", FlashMessage.Status.SUCCESS));
        return "redirect:/projects";
    }

    @RequestMapping("/project_collaborators/{projectId}")
    public String projectCollaborators(@PathVariable Long projectId, Model model) {
        Project project = mProjectService.findById(projectId);
        project.fixCollaboratorsAndRoles();

        List<Collaborator> allCollaborators = mCollaboratorService.findAll();
        List<Assignment> assignments = new ArrayList<>();
        for (Role r : project.getRolesNeeded()) {
            assignments.add(new Assignment(r, allCollaborators, project.getCollaborators()));
        }

        model.addAttribute("page_title", "project collaborates");
        model.addAttribute("assignments", assignments);
        model.addAttribute("project", project);

        return "project_collaborators";
    }

    @RequestMapping(value = "/project_collaborators/{projectId}", method = RequestMethod.POST)
    public String projectCollaborators(@ModelAttribute Project project, @PathVariable Long projectId, BindingResult result, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category",result);
            redirectAttributes.addFlashAttribute("project", project);
            // Redirect back to the form
            return String.format("redirect:/project_collaborators/%s", projectId);
        }

        Project projectToUpdate = mProjectService.findById(projectId);
        List<Collaborator> collaborators = new ArrayList<>();
        // Updating collaborators field
        for (Collaborator collaborator : project.getCollaborators()) {
            if (collaborator.getId()!=null) {
                collaborators.add(mCollaboratorService.findById(collaborator.getId()));
            } else {
                collaborators.add(EMPTY_COLLABORATOR);
            }
        }
        projectToUpdate.setCollaborators(collaborators);

        mProjectService.save(projectToUpdate);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Project collaborators successfully set.", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/project_detail/%s",projectId);
    }

    @RequestMapping("/project_edit/{projectId}")
    public String editProject(@PathVariable Long projectId, Model model) {
        Project project = mProjectService.findById(projectId);
        List<Role> roles = mRoleService.findAll();

        model.addAttribute("page_title", "edit project");
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("project", project);
        return "project_edit";
    }

    @RequestMapping(value = "/project_edit/{projectId}", method = RequestMethod.POST)
    public String editProject(@PathVariable Long projectId, @ModelAttribute Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            // Include validation errors upon redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category",result);
            redirectAttributes.addFlashAttribute("project", project);
            // Redirect back to the form
            return String.format("redirect:/project_edit/%s",projectId);
        }

        Project oldProject = mProjectService.findById(projectId);
        project.setCollaborators(oldProject.getCollaborators());
        mRoleService.setRolesId(project.getRolesNeeded());
        project.fixCollaboratorsAndRoles();
        mProjectService.delete(oldProject);
        projectId = mProjectService.save(project);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Project successfully updated.", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/project_detail/%s",projectId);
    }

    @RequestMapping(value = "project/delete/{projectId}", method = RequestMethod.GET)
    public String deleteProject(@PathVariable Long projectId, RedirectAttributes redirectAttributes) {
        Project project = mProjectService.findById(projectId);

        mProjectService.delete(project);
        redirectAttributes.addFlashAttribute("flash",new FlashMessage("Project successfully deleted.", FlashMessage.Status.SUCCESS));
        return "redirect:/";
    }

}
