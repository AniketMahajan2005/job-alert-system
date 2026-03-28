package com.aniket.job_alert_system.service;

import com.aniket.job_alert_system.model.Job;
import com.aniket.job_alert_system.model.Notification;
import com.aniket.job_alert_system.model.User;
import com.aniket.job_alert_system.repository.NotificationRepository;
import com.aniket.job_alert_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public void notifyMatchedUsers(Job job) {

        // Get all users from database
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {


            if (user.getPreferences() == null || user.getPreferences().isEmpty()) {
                continue;
            }

            // Check if this job matches user's preferences
            // e.g. user preference: "Java, Spring Boot"
            // job skills: "Java, MySQL, Docker"
            // "Java" matches = notify this user
            if (isMatch(job, user)) {

                // Build notification message
                String message = "New job matching your profile! " +
                        job.getTitle() + " at " + job.getCompany() +
                        " — Skills: " + job.getSkillsRequired();

                // Save notification to MySQL
                Notification notification = new Notification();
                notification.setUser(user);
                notification.setJob(job);
                notification.setMessage(message);
                notificationRepository.save(notification);

                // Push real-time notification via WebSocket
                // Each user listens on their own topic: /topic/notifications/their@email.com
                // Right user gets the notification
                messagingTemplate.convertAndSend(
                        "/topic/notifications/" + user.getEmail(),
                        message
                );

                System.out.println(">>> Notified user: " + user.getEmail());
            }
        }
    }

    // Check if job skills match any of the user's preferences
    private boolean isMatch(Job job, User user) {
        // Split user preferences by comma
        // e.g. "Java, Spring Boot, MySQL"  =  ["Java", "Spring Boot", "MySQL"]
        String[] preferences = user.getPreferences().split(",");
        String jobSkills = job.getSkillsRequired().toLowerCase();
        String jobTitle = job.getTitle().toLowerCase();

        for (String preference : preferences) {
            String pref = preference.trim().toLowerCase();
            // Check if job skills or title contains this preference
            if (jobSkills.contains(pref) || jobTitle.contains(pref)) {
                return true;
            }
        }
        return false;
    }
}
