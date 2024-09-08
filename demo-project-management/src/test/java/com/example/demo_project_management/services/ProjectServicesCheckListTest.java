package com.example.demo_project_management.services;
import com.example.demo_project_management.dto.ProjectResponse;
import com.example.demo_project_management.entity.Project;
import com.example.demo_project_management.entity.Task;
import com.example.demo_project_management.enums.Priority;
import com.example.demo_project_management.enums.Status;
import com.example.demo_project_management.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProjectServicesCheckListTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_Success() {
        Project project = new Project();
        project.setId(1);
        project.setName("Project 1");

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ResponseEntity<?> response = projectService.createProject(project);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void createProject_Failure() {
        Project project = new Project();
        doThrow(new RuntimeException("Error")).when(projectRepository).save(any(Project.class));

        ResponseEntity<?> response = projectService.createProject(project);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("some thing wrong"));
    }

    @Test
    void getProject_Success() {
        Project project = new Project();
        project.setId(1);
        project.setName("Project 1");
        project.setDescription("Description 1");

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        ResponseEntity<?> response = projectService.getProject(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProjectResponse projectResponse = (ProjectResponse) response.getBody();
        assertNotNull(projectResponse);
        assertEquals(1, projectResponse.getId());
        assertEquals("Project 1", projectResponse.getName());
    }

    @Test
    void getProject_Failure() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = projectService.getProject(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Project Id not Match", response.getBody());
    }

    @Test
    void getProjectTask_Success() {
        //create project mock
        Project project = new Project();
        project.setId(1);
        project.setName("Project 1");

        //create task
        Task task = new Task();
        task.setProject(project);
        task.setTitle("unit test case");
        task.setDescription("unit testcase descreiption");
        task.setStatus(Status.IN_PROGRESS);
        task.setPriority(Priority.LOW);
        Set<Task> tasks = new HashSet<>();
        tasks.add(task);
        project.setTasks(List.of(task));

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        ResponseEntity<?> response = projectService.getProjectTask(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project.getTasks(), response.getBody());
    }

    @Test
    void getProjectTask_Failure() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = projectService.getProjectTask(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("not found tasks", response.getBody());
    }

    @Test
    void getProjectList_Success() {
        List<Project> projects = new ArrayList<>();
        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Project 1");

        projects.add(project1);

        when(projectRepository.findAll()).thenReturn(projects);

        ResponseEntity<?> response = projectService.getProjectList();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ProjectResponse> projectResponses = (List<ProjectResponse>) response.getBody();
        assertEquals(1, projectResponses.size());
        assertEquals(1, projectResponses.get(0).getId());
    }

    @Test
    void deleteProjectById_Success() {
        Project project = new Project();
        project.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        ResponseEntity<?> response = projectService.deleteProjectById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteProjectById_Failure() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = projectService.deleteProjectById(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody());
    }

    @Test
    void projectUpdateById_Success() {
        Project existingProject = new Project();
        existingProject.setId(1);
        existingProject.setName("Old Project");

        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");
        updatedProject.setDescription("Updated Description");

        when(projectRepository.findById(1)).thenReturn(Optional.of(existingProject));

        ResponseEntity<?> response = projectService.projectUpdateById(1, updatedProject);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(projectRepository, times(1)).save(existingProject);
        assertEquals("Updated Project", existingProject.getName());
        assertEquals("Updated Description", existingProject.getDescription());
    }

    @Test
    void projectUpdateById_Failure() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");

        ResponseEntity<?> response = projectService.projectUpdateById(1, updatedProject);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody());
    }
}
