package com.example.demo_project_management.dto;

import com.example.demo_project_management.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserRegisterDTO {
    @NotNull(message = "Name is required")
    @Size(min = 2, message = "Name must be at least 2 characters")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private List<String> roles;

}
