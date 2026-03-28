package com.aniket.job_alert_system.repository;


import com.aniket.job_alert_system.model.Notification;
import com.aniket.job_alert_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByUserOrderBySentAtDesc(User user);
}
