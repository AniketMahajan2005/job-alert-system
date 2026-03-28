package com.aniket.job_alert_system.service;

import com.aniket.job_alert_system.dto.JobRequest;
import com.aniket.job_alert_system.model.Job;
import com.aniket.job_alert_system.model.User;
import com.aniket.job_alert_system.repository.JobRepository;
import com.aniket.job_alert_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class JobService {

    // Redis key constants — using constants avoids typos
    private static final String ALL_JOBS_KEY = "jobs:all";
    private static final String JOB_SEARCH_KEY = "jobs:search:";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private NotificationService notificationService;

    // POST A JOB (Admin only)
    public Job postJob(JobRequest request) {

        // Build Job object from request
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setSkillsRequired(request.getSkillsRequired());

        // Save to MySQL
        Job savedJob = jobRepository.save(job);

        // Invalidate Redis cache
        // Because we just added a new job, the cached list is now outdated
        // Next time someone fetches jobs, it will reload fresh from MySQL
        redisTemplate.delete(ALL_JOBS_KEY);

        // Also clear any search caches
        // This ensures search results are fresh too
        redisTemplate.keys(JOB_SEARCH_KEY + "*")
                .forEach(key -> redisTemplate.delete(key));

        // Send WebSocket notifications to matched users
        notificationService.notifyMatchedUsers(savedJob);

        return savedJob;
    }

    // GET ALL JOBS (with Redis caching)
    @SuppressWarnings("unchecked")
    public List<Job> getAllJobs() {

        // Step 1: Check Redis first
        List<Job> cachedJobs = (List<Job>) redisTemplate.opsForValue().get(ALL_JOBS_KEY);

        if (cachedJobs != null) {
            System.out.println(">>> Returning jobs from REDIS cache");
            return cachedJobs;  // Return cached data instantly
        }

        // Step 2: Cache miss — get from MySQL
        System.out.println(">>> Cache miss — fetching from MySQL");
        List<Job> jobs = jobRepository.findAll();

        // Step 3: Store in Redis for 10 minutes
        // After 10 minutes it expires automatically
        // TTL = Time To Live
        redisTemplate.opsForValue().set(ALL_JOBS_KEY, jobs, 10, TimeUnit.MINUTES);

        return jobs;
    }

    // SEARCH JOBS BY SKILL (with Redis caching)
    @SuppressWarnings("unchecked")
    public List<Job> searchJobs(String skill) {

        // Each search term gets its own cache key
        // e.g. "jobs:search:Java", "jobs:search:Python"
        String cacheKey = JOB_SEARCH_KEY + skill.toLowerCase();

        // Check Redis first
        List<Job> cachedResults = (List<Job>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedResults != null) {
            System.out.println(">>> Returning search results from REDIS cache");
            return cachedResults;
        }

        // Cache miss — search in MySQL
        List<Job> results = jobRepository.findBySkillsRequiredContainingIgnoreCase(skill);

        // Cache for 5 minutes
        redisTemplate.opsForValue().set(cacheKey, results, 5, TimeUnit.MINUTES);

        return results;
    }

    // GET SINGLE JOB BY ID
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }
}