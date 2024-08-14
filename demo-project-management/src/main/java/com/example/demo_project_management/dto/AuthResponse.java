package com.example.demo_project_management.dto;


import com.example.demo_project_management.enums.Roles;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthResponse {
    private String name;
    private List<String> roles;
    private String token;
}
