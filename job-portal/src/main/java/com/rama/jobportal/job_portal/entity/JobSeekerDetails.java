package com.rama.jobportal.job_portal.entity;


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

    @Lob

    @Column(nullable = true)
    private byte[] resume;

    private double experienceYears;

    private String currentLocation;

    private String preferredLocation;

    @Column(name = "skills", length = 1000)
    private String skills; // Store as comma-separated string


}
