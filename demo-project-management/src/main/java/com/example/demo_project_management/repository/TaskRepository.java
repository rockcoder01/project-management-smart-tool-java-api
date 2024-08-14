package com.example.demo_project_management.repository;

import com.example.demo_project_management.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
