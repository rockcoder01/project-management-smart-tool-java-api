package com.example.demo_project_management.repository;

import com.example.demo_project_management.entity.Role;
import com.example.demo_project_management.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Role findByName(Roles name);
}
