package com.example.demo_project_management.services;

import com.example.demo_project_management.dto.TaskRequest;
import com.example.demo_project_management.entity.Project;
import com.example.demo_project_management.entity.Task;
import com.example.demo_project_management.exception.InvalidOperationException;
import com.example.demo_project_management.repository.ProjectRepository;
import com.example.demo_project_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Task createTask(TaskRequest taskRequest) {
        try {
            Optional<Project> projectOptional = projectRepository.findById(taskRequest.getProject_id());
            if (projectOptional.isEmpty()) {
                throw new RuntimeException("Project not found");
            }
            Project project = projectOptional.get();
            Task task = new Task();
            task.setTitle(taskRequest.getTitle());
            task.setDescription(taskRequest.getDescription());
            task.setStatus(taskRequest.getStatus());
            task.setPriority(taskRequest.getPriority());
            task.setProject(project);
            Task savedTask = taskRepository.save(task);
            List<Task> tasks = project.getTasks();
            tasks.add(savedTask);

            projectOptional.get().setTasks(tasks);
            projectRepository.save(project);

            return savedTask;
        } catch (DataAccessException e) {
            throw new InvalidOperationException("Failed to crate task", e);
        } catch (Exception e) {
            throw new InvalidOperationException("Unexcepted Error occur when create task", e);
        }
    }


    public ResponseEntity<?> getTaskById(Integer id) {
        Optional<Task> taskResponse = taskRepository.findById(id);
        if (taskResponse.isPresent()) {
            return new ResponseEntity<>(taskResponse.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getTaskList() {
        List<Task> taskList = taskRepository.findAll();
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTaskById(Integer id) {
        Optional<Task> taskResponse = taskRepository.findById(id);
        if (taskResponse.isPresent()) {
            try {
                taskRepository.deleteById(id);
                return new ResponseEntity<>("delete Task Successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<?> updateTaskById(Integer id, Task updatedTask) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
//            taskResponse.get().s(taskResponse.getTitle());
            try {
                task.get().setTitle(updatedTask.getTitle());
                task.get().setDescription(updatedTask.getDescription());
                task.get().setStatus(updatedTask.getStatus());
                task.get().setPriority(updatedTask.getPriority());
                Task updatedSaveTask = taskRepository.save(task.get());
                return new ResponseEntity<>(updatedSaveTask, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("something wrong" + e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
    }
}


