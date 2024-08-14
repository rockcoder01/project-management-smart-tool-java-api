package com.example.demo_project_management.repository;

import com.example.demo_project_management.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
