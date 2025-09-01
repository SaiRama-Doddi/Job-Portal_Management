package com.rama.jobportal.job_portal.service;


import com.rama.jobportal.job_portal.entity.Job;
import com.rama.jobportal.job_portal.entity.JobSeekerDetails;
import com.rama.jobportal.job_portal.repository.jobRepository;
import com.rama.jobportal.job_portal.repository.jobSeekerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class recommendedService {

    private final jobRepository  jobRepo;
    private final jobSeekerRepo jobseekerRepo;

    @Transactional(readOnly = true)
    public List<Job> getRecommendedJobs(UUID seekerId) {
        JobSeekerDetails jobSeeker = jobseekerRepo.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("Seeker not found"));

        // Split skills into list
        List<String> skills = Arrays.stream(
                        Optional.ofNullable(jobSeeker.getSkills()).orElse("")
                                .split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        String skill1 = skills.size() > 0 ? skills.get(0).toLowerCase() : null;
        String skill2 = skills.size() > 1 ? skills.get(1).toLowerCase() : null;
        String skill3 = skills.size() > 2 ? skills.get(2).toLowerCase() : null;
        String experience = Optional.ofNullable(jobSeeker.getExperience())
                .map(String::toLowerCase)
                .orElse(null);
        String location = Optional.ofNullable(jobSeeker.getPreferredLocation())
                .map(String::toLowerCase)
                .orElse(null);



        return jobRepo.findRecommendedJobs(skill1, skill2, skill3,
                location, experience);
    }

}
