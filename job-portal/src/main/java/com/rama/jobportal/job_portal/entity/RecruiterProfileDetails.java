package com.rama.jobportal.job_portal.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="recruiter_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfileDetails {

    @Id
    @GeneratedValue
    private UUID recruiterId;

    @OneToOne
   // @MapsId
   // @JoinColumn(name="recruiter_id")
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private String companyName;

    private String companyWebsite;


    @Lob
    @Column(nullable = true)
    private byte[] companyLogo;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String companyDescription;
    private String role;
    private int experienceNeeded;



    @OneToMany(mappedBy = "recruiterProfileDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Job> jobs=new ArrayList<>();

}
