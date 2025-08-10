package com.rama.jobportal.job_portal.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruiter")

public class RecruiterController {

    @GetMapping("/profile")
    public String getProfile() {
        return "Recruiterr profile data";
    }
}
