package com.rama.jobportal.job_portal.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="recruiter_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfileDetails {

    @Id
    private UUID recruiterId;

    @OneToOne
    @MapsId
    @JoinColumn(name="recruiter_id")
    private User user;

    private String companyName;

    private String companyWebsite;


    @Lob
    @Column(nullable = true)
    private byte[] companyLogo;

}
