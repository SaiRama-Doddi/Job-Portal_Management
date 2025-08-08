package com.rama.jobportal.job_portal.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class recruiterDTO {

    private String companyName;
    private String companyWebsite;
    private MultipartFile companyLogo;
   private String companyDescription;
    private String role;
    private int experienceNeeded;
}
