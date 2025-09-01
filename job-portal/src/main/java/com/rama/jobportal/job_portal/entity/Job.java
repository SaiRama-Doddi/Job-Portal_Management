package com.rama.jobportal.job_portal.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="job")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"companyLogo", "jobDescription"})
public class Job {


    @Id
    @GeneratedValue
    private UUID jobId;


    private String jobTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    private String location;

    private String salary;

    private String employmentType;

    public String experienceNeeded;

    @Column(name = "skills", length = 1000)
    private String skills; // Store as comma-separated string


    private String companyName;

    @Lob
    @Column(nullable = true)
    @Basic(fetch = FetchType.EAGER)
    private byte[] companyLogo;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", referencedColumnName = "recruiterId")
    private RecruiterProfileDetails recruiterProfileDetails;
}
