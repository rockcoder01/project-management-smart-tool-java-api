package com.example.demo_project_management.services;

import com.example.demo_project_management.dto.ProjectResponse;
import com.example.demo_project_management.entity.Project;
import com.example.demo_project_management.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;


    public ResponseEntity<?> createProject(Project project) {
        try {
            Project savedProject = projectRepository.save(project);
            return new ResponseEntity<>(savedProject, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("some thing wrong" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getProject(Integer id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setId(project.get().getId());
            projectResponse.setName(project.get().getName());
            projectResponse.setDescription(project.get().getDescription());
            projectResponse.setCreatedAt(project.get().getCreatedAt());
            projectResponse.setUpdatedAt(project.get().getUpdatedAt());

            return new ResponseEntity<>(projectResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Project Id not Match", HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> getProjectTask(Integer id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isPresent() && !project.get().getTasks().isEmpty()) {
            return new ResponseEntity<>(project.get().getTasks(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("not found tasks", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getProjectList() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> projectResponsesList = projects.stream()
                .map(project -> {
                    ProjectResponse projectResponse = new ProjectResponse();
                    projectResponse.setId(project.getId());
                    projectResponse.setName(project.getName());
                    projectResponse.setDescription(project.getDescription());
                    projectResponse.setCreatedAt(project.getCreatedAt());
                    projectResponse.setUpdatedAt(project.getUpdatedAt());
                    return projectResponse;
                }).collect(Collectors.toList());
        return new ResponseEntity<>(projectResponsesList, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteProjectById(Integer id) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isPresent()){
            projectRepository.deleteById(id);
           return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> projectUpdateById(Integer id, Project updatedProject) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isPresent()){
            project.get().setName(updatedProject.getName());
            project.get().setDescription(updatedProject.getDescription());
            projectRepository.save(project.get());

            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
