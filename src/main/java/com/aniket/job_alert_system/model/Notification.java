package com.aniket.job_alert_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne                           // Many notifications can belong to one user
    @JoinColumn(name = "user_id")        // Foreign key column in MySQL
    private User user;

    @ManyToOne                           // Many notifications can be about one job
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(nullable = false)
    private String message;              // e.g. "New job matching your profile!"

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @PrePersist
    public void prePersist() {
        sentAt = LocalDateTime.now();
    }
}