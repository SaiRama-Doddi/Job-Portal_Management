package com.rama.jobportal.job_portal.service;


import com.rama.jobportal.job_portal.dto.jobDTO;
import com.rama.jobportal.job_portal.entity.Job;
import com.rama.jobportal.job_portal.entity.RecruiterProfileDetails;
import com.rama.jobportal.job_portal.repository.jobRepository;
import com.rama.jobportal.job_portal.repository.recruiterProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class jobService {

    private  final jobRepository jobRepo;
    private final recruiterProfileRepo recruiterprofileRepo;


    public  void createJob(UUID recruiterId, jobDTO jobDto, MultipartFile companyLogo) throws IndexOutOfBoundsException, IOException {
        Optional<RecruiterProfileDetails> rp=recruiterprofileRepo.findById(recruiterId);
        if(rp.isEmpty()) throw  new RuntimeException("Recruiter not found");

        RecruiterProfileDetails recruiter=rp.get();

        String skills=String.join("," ,jobDto.getSkills());

        Job job=Job.builder()
                .jobDescription(jobDto.getDescription())
                .jobTitle(jobDto.getTitle())
                .location(jobDto.getLocation())
                .salary(jobDto.getSalary())
                .employmentType(jobDto.getEmploymentType())
                .experienceNeeded(jobDto.getExperienceNeeded())
                .skills(skills)
                .companyName(jobDto.getCompanyName())
                .companyLogo(companyLogo.getBytes())
                .recruiterProfileDetails(recruiter)
                .build();


        jobRepo.save(job);
    }



}
