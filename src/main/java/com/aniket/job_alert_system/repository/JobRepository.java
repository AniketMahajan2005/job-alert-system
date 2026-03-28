package com.aniket.job_alert_system.repository;


import com.aniket.job_alert_system.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {


    List<Job> findBySkillsRequiredContainingIgnoreCase(String keyword);

    @Query("SELECT j FROM Job j WHERE " +
            "LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :preference, '%')) OR " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', :preference, '%'))")
    List<Job> findJobsMatchingPreference(String preference);
}
