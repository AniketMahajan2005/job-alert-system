package com.aniket.job_alert_system.controller;


import com.aniket.job_alert_system.model.Notification;
import com.aniket.job_alert_system.model.User;
import com.aniket.job_alert_system.repository.NotificationRepository;
import com.aniket.job_alert_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/my")
    public ResponseEntity<List<Notification>> getMyNotifications() {

        // Get the logged-in user's email from JWT
        // Remember JwtFilter sets this in SecurityContextHolder
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Find user in database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get all notifications for this user, newest first
        List<Notification> notifications =
                notificationRepository.findByUserOrderBySentAtDesc(user);

        return ResponseEntity.ok(notifications);
    }
}
