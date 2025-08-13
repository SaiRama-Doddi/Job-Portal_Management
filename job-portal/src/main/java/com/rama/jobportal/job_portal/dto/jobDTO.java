package com.rama.jobportal.job_portal.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class jobDTO {

    private String title;
    private  String description;
    private String location;
    private String salary;
    private String employmentType;
    public String experienceNeeded;
    private List<String> skills;

    private  String companyName;
    private UUID recruiterId;
}
