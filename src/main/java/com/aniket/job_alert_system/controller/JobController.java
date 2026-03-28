package com.aniket.job_alert_system.controller;

import com.aniket.job_alert_system.dto.JobRequest;
import com.aniket.job_alert_system.model.Job;
import com.aniket.job_alert_system.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // POST /api/jobs/post
    // Admin only — protected in SecurityConfig
    @PostMapping("/post")
    public ResponseEntity<Job> postJob(@Valid @RequestBody JobRequest request) {
        Job job = jobService.postJob(request);
        return ResponseEntity.ok(job);
    }

    // GET /api/jobs/all
    // Any authenticated user can see jobs
    @GetMapping("/all")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    // GET /api/jobs/search?skill=Java
    // Search jobs by skill keyword
    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String skill) {
        List<Job> jobs = jobService.searchJobs(skill);
        return ResponseEntity.ok(jobs);
    }

    // GET /api/jobs/{id}
    // Get single job by ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }
}
