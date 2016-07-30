package com.zzheads.instateam.web.controller;//

import com.zzheads.instateam.model.*;
import com.zzheads.instateam.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    private boolean firstStart = true;

    // Index
    @RequestMapping("/")
    public String index(Model model) {
        if (firstStart) {
            EMPTY_ROLE = new Role(EMPTY_ROLE_TEXT);
            EMPTY_COLLABORATOR = new Collaborator(EMPTY_COLLABORATOR_TEXT, EMPTY_ROLE);
            if (mRoleService.findByName(EMPTY_ROLE_TEXT)==null) mRoleService.save(EMPTY_ROLE);
            if (mCollaboratorService.findByName(EMPTY_COLLABORATOR_TEXT)==null) mCollaboratorService.save(EMPTY_COLLABORATOR);
            EMPTY_ROLE.setId(mRoleService.findByName(EMPTY_ROLE_TEXT).getId());
            EMPTY_COLLABORATOR.setId(mCollaboratorService.findByName(EMPTY_COLLABORATOR_TEXT).getId());
            firstStart = false;
        }
        model.addAttribute("page_title", "index");
        model.addAttribute("projects", mProjectService.findAll());
        model.addAttribute("statuses", Status.values());
        return "index";
    }

    @RequestMapping("/project_detail/{projectId}")
    public String projectDetails(@PathVariable Long projectId, Model model) {
        Project project = mProjectService.findById(projectId);
        model.addAttribute("page_title", "project details");
        model.addAttribute("project", project);
        return "project_detail";
    }

    @RequestMapping("/project_new")
    public String createProject(Model model) {
        Project project = new Project ();
        List<Role> roles = mRoleService.findAll();

        model.addAttribute("page_title", "create project");
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("project", project);
        return "project_new";
    }

    @RequestMapping(value = "/project_new", method = RequestMethod.POST)
    public String createProject(@ModelAttribute Project project, Model model) {
        // Fill id field for project.rolesNeeded
        mRoleService.setRolesId(project.getRolesNeeded());
        project.fixCollaboratorsAndRoles();

        mProjectService.save(project);
        return "redirect:/";
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
    public String projectCollaborators(@ModelAttribute Project project, @PathVariable Long projectId) {
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
        return String.format("redirect:/project_detail/%s",projectId);
    }

    @RequestMapping("/edit_project/{projectId}")
    public String editProject(@PathVariable Long projectId, Model model) {
        Project project = mProjectService.findById(projectId);
        List<Role> roles = mRoleService.findAll();

        model.addAttribute("page_title", "edit project");
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("project", project);
        return "edit_project";
    }

    @RequestMapping(value = "/edit_project/{projectId}", method = RequestMethod.POST)
    public String editProject(@PathVariable Long projectId, @ModelAttribute Project project) {
        Project oldProject = mProjectService.findById(projectId);

        project.setCollaborators(oldProject.getCollaborators());

        mRoleService.setRolesId(project.getRolesNeeded());
        project.fixCollaboratorsAndRoles();

        mProjectService.delete(oldProject);
        projectId = mProjectService.save(project);
        return String.format("redirect:/project_detail/%s",projectId);
    }

}
