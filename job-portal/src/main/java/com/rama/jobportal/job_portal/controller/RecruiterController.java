package com.rama.jobportal.job_portal.controller;


import com.rama.jobportal.job_portal.dto.recruiterDTO;
import com.rama.jobportal.job_portal.service.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private  final userService UserService;

    @GetMapping("/profile")
    public String getProfile() {
        return "Recruiter profile data";
    }


    @PutMapping(value = "/update/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> recruiterUpdate(
            @PathVariable UUID userId,
            @RequestPart("recruiter") recruiterDTO recruiterDto,
            @RequestPart(value = "companyLogo", required = false) MultipartFile companyLogo

            ){

        try{
            UserService.updateRecruiter(userId,recruiterDto,companyLogo);
            return ResponseEntity.ok("recruiter details updated successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
