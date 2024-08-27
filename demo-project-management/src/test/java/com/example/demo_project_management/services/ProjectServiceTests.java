package com.example.demo_project_management.services;

import com.example.demo_project_management.entity.Project;
import com.example.demo_project_management.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void getProjectTest() {
        Project mockProject = new Project();
        mockProject.setId(1);
        mockProject.setName("Test Project");
        mockProject.setDescription("233 Test Project");

        when(projectRepository.findById(1)).thenReturn(Optional.of(mockProject));

        ResponseEntity<?> response = projectService.getProject(1);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody(), "Response body should be null");
        System.out.println(mockProject.getName());

    }
}
