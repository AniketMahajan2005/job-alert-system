package com.aniket.job_alert_system.repository;


import com.aniket.job_alert_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
