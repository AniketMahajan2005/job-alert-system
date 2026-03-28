package com.aniket.job_alert_system.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Location is required")
    private String location;

    private String description;

    @NotBlank(message = "Skills are required")
    private String skillsRequired;
}
