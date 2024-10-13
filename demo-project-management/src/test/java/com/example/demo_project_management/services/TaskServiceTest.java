package com.example.demo_project_management.services;

import com.example.demo_project_management.dto.ProjectResponse;
import com.example.demo_project_management.dto.TaskRequest;
import com.example.demo_project_management.entity.Project;
import com.example.demo_project_management.entity.Task;
import com.example.demo_project_management.entity.User;
import com.example.demo_project_management.enums.Priority;
import com.example.demo_project_management.enums.Status;
import com.example.demo_project_management.exception.InvalidOperationException;
import com.example.demo_project_management.repository.ProjectRepository;
import com.example.demo_project_management.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;


    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void createTask() {
        Project project = new Project();
        project.setId(1);
        project.setName("Project 1");
        project.setDescription("Description 1");
        project.setTasks(new ArrayList<>());  // Ensure tasks list is initialized

        Task task = new Task();
        task.setTitle("Demo Project");
        task.setDescription("test");
        task.setStatus(Status.IN_PROGRESS);
        task.setPriority(Priority.LOW);
        task.setProject(project);

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("Demo Project");
        taskRequest.setDescription("test");
        taskRequest.setPriority(Priority.LOW);
        taskRequest.setStatus(Status.IN_PROGRESS);
        taskRequest.setProject_id(1);


        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Task taskResponse = taskService.createTask(taskRequest);

        //assert
        assertEquals(task, taskResponse);
        assertEquals(task.getTitle(), taskResponse.getTitle());
        assertEquals(task.getDescription(), taskResponse.getDescription());
        assertEquals(task.getStatus(), taskResponse.getStatus());
        assertEquals(task.getPriority(), taskResponse.getPriority());
        assertEquals(task.getProject().getId(), taskResponse.getProject().getId());

    }

    @Test
    public void getTaskById() {
        Task task = new Task();
        task.setTitle("Demo Project");
        task.setDescription("Title");
        task.setPriority(Priority.MEDIUM);
        task.setStatus(Status.IN_PROGRESS);
        task.setId(1);

        //when
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        ResponseEntity<?> taskResponse = taskService.getTaskById(1);


        //assert
        assertEquals(HttpStatus.OK, taskResponse.getStatusCode());
        Task responseTask = (Task) taskResponse.getBody();
        assertEquals("Demo Project", responseTask.getTitle());
        assertEquals("Title", responseTask.getDescription());
        assertEquals(Priority.MEDIUM, responseTask.getPriority());
        assertEquals(Status.IN_PROGRESS, responseTask.getStatus());

    }


    @Test
    public void getTaskList() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Demo Project");
        task.setDescription("Title");
        task.setPriority(Priority.MEDIUM);
        task.setStatus(Status.IN_PROGRESS);
        task.setId(1);

        Task task2 = new Task();
        task2.setTitle("Demo Project 2");
        task2.setDescription("Title 2");
        task2.setPriority(Priority.MEDIUM);
        task2.setStatus(Status.IN_PROGRESS);
        task2.setId(2);

        tasks.add(task);
        tasks.add(task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        ResponseEntity<?> taskResponseEntity = taskService.getTaskList();
        //assert
        assertEquals(HttpStatus.OK, taskResponseEntity.getStatusCode());
        List<Task> tasksResponseList = (List<Task>) taskResponseEntity.getBody();
        assertEquals(task.getTitle(), tasksResponseList.get(0).getTitle());
        assertEquals(2, tasksResponseList.size());
        assertEquals(1, tasksResponseList.get(0).getId());
        assertEquals(2, tasksResponseList.get(1).getId());

    }
}
