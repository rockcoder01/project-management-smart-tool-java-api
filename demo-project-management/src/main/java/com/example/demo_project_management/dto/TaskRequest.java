package com.example.demo_project_management.dto;

import com.example.demo_project_management.enums.Priority;
import com.example.demo_project_management.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private int project_id;
}
