package com.rama.jobportal.job_portal.controller;

import com.rama.jobportal.job_portal.dto.jobSeekerDTO;
import com.rama.jobportal.job_portal.entity.Job;
import com.rama.jobportal.job_portal.service.recommendedService;
import com.rama.jobportal.job_portal.service.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobseeker")
@RequiredArgsConstructor
public class JobSeekerController {

    private  final userService UserService;

    private final recommendedService RecommendedService;

    @GetMapping("/profile")
    public String getProfile() {
        return "Jobseeker profile data";
    }

    @PutMapping(value = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateJobSeekerDetails(
            @PathVariable UUID userId,
            @RequestPart("jobSeeker") jobSeekerDTO jobSeekerDto,
            @RequestPart(value = "resume", required = false) MultipartFile resume) {
        try {
            UserService.updateJobSeeker(userId, jobSeekerDto, resume);
            return ResponseEntity.ok("Job seeker details updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing resume file.");
        }
    }


    @GetMapping("/recommend/{seekerId}")
    public List<Job> getRecommendedJobs(@PathVariable UUID seekerId){
        return RecommendedService.getRecommendedJobs(seekerId);
    }



}
