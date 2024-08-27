package com.example.demo_project_management.controller;

import com.example.demo_project_management.entity.Project;
import com.example.demo_project_management.services.ProjectService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
@CrossOrigin("http://localhost:4200")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    private ResponseEntity<?> createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getProject(@PathVariable("id") Integer id) {
        return projectService.getProject(id);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteProjectById(@PathVariable("id") Integer id) {
        return projectService.deleteProjectById(id);
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> projectUpdateById(@PathVariable("id") Integer id, @RequestBody Project project) {
        return projectService.projectUpdateById(id, project);
    }

    @GetMapping("/list")
    private ResponseEntity<?> getProjectList(){
        return projectService.getProjectList();
    }

    @GetMapping("/tasks/{id}")
    private ResponseEntity<?> getProjectTask(@PathVariable("id") Integer id) {
        return projectService.getProjectTask(id);
    }



}
