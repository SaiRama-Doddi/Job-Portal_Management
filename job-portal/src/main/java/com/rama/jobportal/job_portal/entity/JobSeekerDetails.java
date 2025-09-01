package com.rama.jobportal.job_portal.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="job_seeker_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSeekerDetails {

    @Id
    @GeneratedValue
    private UUID seekerId;

    @OneToOne
   // @MapsId
    //@JoinColumn(name="seeker_id")
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private  User user;

    private String name;
    private String gender;

    @Lob
    @Column(nullable = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private byte[] resume;

// Before:
// private double experienceYears;

    // After:
    private String experience; // store values like "Fresher", "0-1 years", "2 years"


    private String currentLocation;

    private String preferredLocation;

    @Column(name = "skills", length = 1000)
    private String skills; // Store as comma-separated string


}
