package com.example.demo_project_management.controller;

import com.example.demo_project_management.dto.TaskRequest;
import com.example.demo_project_management.entity.Task;
import com.example.demo_project_management.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/task")
@CrossOrigin("http://localhost:4200")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @GetMapping("/{id}")
    private ResponseEntity<?> getTaskById(@PathVariable("id") Integer id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    private ResponseEntity<?> getTasksList(){
        return taskService.getTaskList();
    }

    @PostMapping("/add")
    private ResponseEntity<?> createTask(@RequestBody TaskRequest task) {
        Task taskResponse = taskService.createTask(task);
        return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{projectId}/{id}")
    private ResponseEntity<?> deleteTaskById(@PathVariable("projectId") Integer projectId, @PathVariable("id") Integer id) {
        return taskService.deleteTaskById(projectId,id);
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateTaskById(@PathVariable("id") Integer id,
                                @RequestBody Task updatedTask){
        return taskService.updateTaskById(id, updatedTask);
    }





}
