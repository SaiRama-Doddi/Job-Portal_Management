package com.rama.jobportal.job_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class jobSeekerDTO {

  //  private double experienceYears;
  private String experience; // e.g., "Fresher", "0-1 years", "2 years"
    private String currentLocation;
    private String preferredLocation;
    private List<String> skills;

    private String name;
    private String gender;
}
