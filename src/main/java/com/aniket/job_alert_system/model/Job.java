package com.aniket.job_alert_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "jobs")
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String location;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String skillsRequired;

    @Column(nullable = false)
    private LocalDateTime postedAt;
//    @Column(nullable = false, columnDefinition = "DATETIME")
//    private LocalDateTime postedAt;


    @PrePersist                          // Runs automatically before saving to DB
    public void prePersist() {
        postedAt = LocalDateTime.now();  // Auto-sets the time
    }




}
