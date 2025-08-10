package com.rama.jobportal.job_portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobseeker")
public class JobSeekerController {

    @GetMapping("/profile")
    public String getProfile() {
        return "Jobseeker profile data";
    }
}
